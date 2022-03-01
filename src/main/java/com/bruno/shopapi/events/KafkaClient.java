package com.bruno.shopapi.events;

import com.bruno.shopapi.dto.ShopDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaClient {
    private final KafkaTemplate<String, ShopDTO> kafkaTemplate;
    private final String SHOP_TOPIC_NAME = "SHOP_TOPIC";

    public void sendMessage(ShopDTO msg){
        kafkaTemplate.send(SHOP_TOPIC_NAME, msg);
    }
}
