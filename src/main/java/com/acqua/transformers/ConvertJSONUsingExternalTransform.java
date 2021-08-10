package com.acqua.transformers;

import java.nio.charset.StandardCharsets;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.MessageFactory;
import org.mule.transformer.AbstractMessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
  
import com.atina.convertPDFtoJSON.servicios.PDFConvertToJSONServiceGrpc;
import com.atina.convertPDFtoJSON.servicios.TransformRequest;
import com.atina.convertPDFtoJSON.servicios.TransformResponse;
import com.atina.convertPDFtoJSON.servicios.PDFConvertToJSONServiceGrpc.PDFConvertToJSONServiceBlockingStub;
import com.google.shade.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ConvertJSONUsingExternalTransform extends AbstractMessageTransformer {
	
	private static final Logger logger = LoggerFactory.getLogger(ConvertJSONUsingExternalTransform.class);
	
	@Value("${convert_json_Service_host}")
	private String serviceHost;

	@Value("${convert_json_Service_port}")
	private int servicePort;
	
	
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
	
	
		
	//-------------------- CARGO TEMPLEATE DE DATASONNET PARA MAPEAR------------------------------	
		
	byte[] transformTemplate = null;

		
	try {

		logger.debug("    Loading transform script...");
		
		if(message.getInvocationProperty("transform")!=null)
		{
			
			if (message.getInvocationProperty("transform").getClass().getSimpleName().equals("byte[]")) {

				transformTemplate = (byte[]) message.getInvocationProperty("template");
				
				logger.debug("    Transform script has been loaded");

			}

			if (message.getInvocationProperty("transform") instanceof java.lang.String) {

				final String inputTemplateStr = (String) message.getInvocationProperty("transform");

				transformTemplate = inputTemplateStr.getBytes(StandardCharsets.UTF_8);
				
				logger.debug("    Transform script has been loaded");

			}
		
		} else
		{
			logger.debug("    There is not transform");
		}


	} catch (

		final Exception e) {

			logger.error("ERROR Reading XML Transform: " + e.getMessage(),e);
		
			throw new TransformerException(MessageFactory.createStaticMessage("Invalid Tranform"), e);

	}
	
	//----------------------------CARGO INPUT JSON----------------------------------------------------------
	
	ByteString jsonResponse = null;
	byte[] jsonResponseByte = null;
	String inputXMLAsJSON = null;
	
	
	try {
		
		if (message.getInvocationProperty("transform") instanceof java.lang.String) {

			inputXMLAsJSON = (String) message.getPayload();

			jsonResponseByte = inputXMLAsJSON.getBytes(StandardCharsets.UTF_8);
			
			jsonResponse = ByteString.copyFrom(jsonResponseByte);
			
			logger.debug("    Transform script has been loaded");

		}
		

		logger.debug("JSON loaded");

	} catch (final Exception e) {
		
			logger.error("ERROR Loading input JSON: " + e.getMessage(),e);

			throw new TransformerException(MessageFactory.createStaticMessage("Invalid JSON"),e);

	}
	
	
	
	
	//---------------------------------LLAMO AL SERVICIO----------------------------------------------------
	
	
			// ===========================
			// Convert JSON With DataSonnet
			// ===========================
			// 
			
			byte[] jsonTransformedConverted = null;
			String returnValue = "{}";
			
			
			long uuid = System.currentTimeMillis();
			
			ManagedChannel channel = null;
			
			PDFConvertToJSONServiceBlockingStub stub = null;
			 
			if(transformTemplate != null)
			{
				 
				try {
					
					// ===========================
					// Create Chanel
					// ===========================
					//
					channel = ManagedChannelBuilder.forAddress(serviceHost, servicePort).usePlaintext().build();
					
					logger.debug("Service Channel has been created");
					
					// ===========================
					// Creacion del Stub
					// ===========================
					//
					stub = PDFConvertToJSONServiceGrpc.newBlockingStub(channel);
					
					logger.debug("Stub has been created");
					 
					// ===========================
					// Convert XML WIth Template
					// ===========================
					//
					
					logger.debug("Transforming JSON using script ...");
					
					
					TransformResponse transform = stub.transformJSON(TransformRequest.newBuilder()
							.setFileInputJSON(jsonResponse)  //HASTA ACA LLEGO
							.setFileTransformAsDS(ByteString.copyFrom(transformTemplate))
							.setTransactionID(uuid).build());
		
					// -----------------------------------------------------
					// Return JSON converted
					// -----------------------------------------------------
					//
					
					logger.debug("JSON converted using script has been processed");
		
					ByteString jsonTransformedResponse = transform.getFileJSONTransformed();
		
					jsonTransformedConverted = jsonTransformedResponse.toByteArray();
		
					returnValue = new String(jsonTransformedConverted, StandardCharsets.UTF_8);
					
					logger.debug("JSON value has been processed");
					
		
				} catch (
		
					final Exception e) {
			
						logger.error("ERROR Converting JSON: " + e.getMessage(),e);
					
						throw new TransformerException(MessageFactory.createStaticMessage(e.getMessage()),
								e);
		
				}

			}
			
			return returnValue;
		}
	
}
