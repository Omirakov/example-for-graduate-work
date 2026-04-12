package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.*;

@RestController
@RequestMapping("/ads")
@Tag(name = "Объявления", description = "API для работы с объявлениями")
public class AdController {

    private static final String DEFAULT_IMAGE_URL = "/images/ad/%d.jpg";

    @Operation(summary = "Получение всех объявлений", description = "Возвращает список всех объявлений", responses = {@ApiResponse(responseCode = "200", description = "Успешный ответ", content = @Content(schema = @Schema(implementation = Ads.class)))})
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(new Ads());
    }

    @Operation(summary = "Добавление объявления", description = "Создаёт новое объявление с заголовком, ценой, описанием и изображением", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Форма с данными объявления и изображением", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(type = "object", description = "Объект, содержащий поля 'properties' и 'image'"))), responses = {@ApiResponse(responseCode = "201", description = "Объявление успешно создано", content = @Content(schema = @Schema(implementation = Ad.class))), @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Ad> addAd(@RequestPart("properties") CreateOrUpdateAd properties, @RequestPart("image") byte[] image, Authentication authentication) {

        System.out.println("✅ Принято объявление: " + properties.getTitle());
        System.out.println("✅ Размер изображения: " + image.length + " байт");
        System.out.println("✅ Автор: " + authentication.getName());

        Ad ad = new Ad();
        ad.setPk(1); // будет заменено на генерацию ID
        ad.setTitle(properties.getTitle());
        ad.setPrice(properties.getPrice());
        ad.setAuthor(1); // будет заменено на ID пользователя
        ad.setImage(String.format(DEFAULT_IMAGE_URL, ad.getPk()));

        return ResponseEntity.status(201).body(ad);
    }

    @Operation(summary = "Получение информации об объявлении", description = "Возвращает полную информацию об объявлении по его ID", responses = {@ApiResponse(responseCode = "200", description = "Успешный ответ", content = @Content(schema = @Schema(implementation = ExtendedAd.class))), @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"), @ApiResponse(responseCode = "404", description = "Объявление не найдено")})
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(new ExtendedAd());
    }

    @Operation(summary = "Удаление объявления", description = "Удаляет объявление по ID (только для автора или администратора)", responses = {@ApiResponse(responseCode = "204", description = "Объявление успешно удалено"), @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"), @ApiResponse(responseCode = "403", description = "Доступ запрещён"), @ApiResponse(responseCode = "404", description = "Объявление не найдено")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAd(@PathVariable("id") Integer id) {
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновление информации об объявлении", description = "Изменяет заголовок, цену и описание объявления", responses = {@ApiResponse(responseCode = "200", description = "Объявление успешно обновлено", content = @Content(schema = @Schema(implementation = Ad.class))), @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"), @ApiResponse(responseCode = "403", description = "Доступ запрещён"), @ApiResponse(responseCode = "404", description = "Объявление не найдено")})
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(@PathVariable("id") Integer id, @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        return ResponseEntity.ok(new Ad());
    }

    @Operation(summary = "Получение объявлений авторизованного пользователя", description = "Возвращает список всех объявлений текущего пользователя", responses = {@ApiResponse(responseCode = "200", description = "Успешный ответ", content = @Content(schema = @Schema(implementation = Ads.class))), @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")})
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe() {
        return ResponseEntity.ok(new Ads());
    }

    @Operation(summary = "Обновление картинки объявления", description = "Заменяет изображение объявления", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Форма с новым изображением", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(type = "object", description = "Объект, содержащий поле 'image'"))), responses = {@ApiResponse(responseCode = "200", description = "Изображение успешно обновлено"), @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"), @ApiResponse(responseCode = "403", description = "Доступ запрещён"), @ApiResponse(responseCode = "404", description = "Объявление не найдено")})
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateImage(@PathVariable("id") Integer id, @RequestPart("image") byte[] image) {
        System.out.println("✅ Изображение обновлено, размер: " + image.length);
        return ResponseEntity.ok(image);
    }
}