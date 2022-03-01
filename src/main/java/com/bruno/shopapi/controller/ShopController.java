package com.bruno.shopapi.controller;

import com.bruno.shopapi.dto.ShopDTO;
import com.bruno.shopapi.events.KafkaClient;
import com.bruno.shopapi.model.Shop;
import com.bruno.shopapi.model.ShopItem;
import com.bruno.shopapi.repository.ShopRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopRepository shopRepository;
    private final KafkaClient kafkaClient;

    @GetMapping
    public List<ShopDTO> getShop(){
        return shopRepository
                .findAll()
                .stream()
                .map(shop -> ShopDTO.convert(shop))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ShopDTO saveShop(@RequestBody ShopDTO shopDTO){
        shopDTO.setIdentifier(UUID.randomUUID().toString());
        shopDTO.setDateShop(LocalDate.now());
        shopDTO.setStatus("PENDING");

        Shop shop = Shop.convert((shopDTO));
        for(ShopItem shopItem : shop.getItems()){
            shopItem.setShop(shop);
        }

        shopDTO = ShopDTO.convert(shopRepository.save(shop));
        kafkaClient.sendMessage(shopDTO);

        return shopDTO;
    }
}
