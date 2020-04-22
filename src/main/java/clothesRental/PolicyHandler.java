package clothesRental;

import clothesRental.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    @Autowired
    DeliveryProcessingRepository deliveryProcessingRepository;
    DeliveryProcessing deliveryProcessing = new DeliveryProcessing();

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentSuccess_DeliveryStart(@Payload PaymentSuccess paymentSuccess){

        if(paymentSuccess.isMe()){
            System.out.println("##### listener DeliveryStart : " + paymentSuccess.toJson());

            //deliveryProcessing.setStatus("Delivery Start");

            deliveryProcessing.setStatus(paymentSuccess.getEventType());


            deliveryProcessing.setId(paymentSuccess.getId());
            deliveryProcessing.setOrderId(paymentSuccess.getOrderId());
            deliveryProcessing.setProductId(paymentSuccess.getProductId());
            deliveryProcessing.setCustomerNm(paymentSuccess.getCustoemrNm());
            deliveryProcessing.setAddress(paymentSuccess.getAddress());
            deliveryProcessingRepository.save(deliveryProcessing);

            deliveryProcessing.setStatus("Delivery End");

            System.out.println("##### listener 배달 완료 : " + paymentSuccess.toJson());

        }
    }
}
