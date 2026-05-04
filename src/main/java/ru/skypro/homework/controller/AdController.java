package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
@Tag(name = "Объявления", description = "API для работы с объявлениями")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;
    private final ImageService imageService;
    private final AdMapper adMapper; // Добавлено: теперь используется adMapper

    @Operation(summary = "Получение всех объявлений", responses = {@ApiResponse(responseCode = "200", description = "Успешный ответ", content = @Content(schema = @Schema(implementation = Ad.class)))})
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @Operation(summary = "Создание объявления", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)), responses = {@ApiResponse(responseCode = "201", description = "Объявление создано"), @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Ad> createAd(@RequestPart("properties") @Valid CreateOrUpdateAd createOrUpdateAd, @RequestPart("image") MultipartFile image, Authentication authentication) throws IOException {

        AdEntity ad = adMapper.toEntity(createOrUpdateAd);
        Ad createdAd = adService.createAd(ad, authentication.getName());
        imageService.saveAdImage(createdAd.getPk(), image.getBytes());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAd);
    }

    @Operation(summary = "Получение информации об объявлении", responses = {@ApiResponse(responseCode = "200", description = "Успешный ответ", content = @Content(schema = @Schema(implementation = ExtendedAd.class))), @ApiResponse(responseCode = "404", description = "Объявление не найдено")})
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAd(@PathVariable Integer id) {
        ExtendedAd ad = adService.getExtendedAd(id);
        return ResponseEntity.ok(ad);
    }

    @Operation(summary = "Удаление объявления", responses = {@ApiResponse(responseCode = "204", description = "Объявление удалено"), @ApiResponse(responseCode = "403", description = "Доступ запрещён")})
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteAd(@PathVariable Integer id, Authentication authentication) {
        adService.deleteAd(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновление информации об объявлении", responses = {@ApiResponse(responseCode = "200", description = "Объявление обновлено"), @ApiResponse(responseCode = "403", description = "Доступ запрещён")})
    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Ad> updateAd(@PathVariable Integer id, @RequestBody CreateOrUpdateAd update, Authentication authentication) {
        AdEntity adEntity = adMapper.toEntity(update);
        Ad updatedAd = adService.updateAd(id, adEntity, authentication.getName());
        return ResponseEntity.ok(updatedAd);
    }

    @Operation(summary = "Обновление изображения объявления", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)), responses = {@ApiResponse(responseCode = "200", description = "Изображение обновлено"), @ApiResponse(responseCode = "403", description = "Доступ запрещён")})
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Ad> updateImage(@PathVariable Integer id, @RequestParam("image") MultipartFile image, Authentication authentication) throws IOException {
        Ad updatedAd = adService.updateImage(id, image.getBytes(), authentication.getName());
        return ResponseEntity.ok(updatedAd);
    }
}