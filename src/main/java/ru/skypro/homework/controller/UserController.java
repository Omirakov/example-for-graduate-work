package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "API для управления профилем пользователя")
public class UserController {

    @Operation(summary = "Обновление пароля", description = "Изменяет пароль текущего пользователя после проверки текущего пароля", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NewPassword.class))), responses = {@ApiResponse(responseCode = "200", description = "Пароль успешно изменён"), @ApiResponse(responseCode = "401", description = "Текущий пароль неверен"), @ApiResponse(responseCode = "403", description = "Доступ запрещён")})
    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@RequestBody NewPassword newPassword) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение информации об авторизованном пользователе", description = "Возвращает данные текущего аутентифицированного пользователя", responses = {@ApiResponse(responseCode = "200", description = "Успешный ответ", content = @Content(schema = @Schema(implementation = User.class))), @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")})
    @GetMapping("/me")
    public ResponseEntity<User> getUser() {
        return ResponseEntity.ok(new User());
    }

    @Operation(summary = "Обновление информации об авторизованном пользователе", description = "Изменяет имя, фамилию и телефон текущего пользователя", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UpdateUser.class))), responses = {@ApiResponse(responseCode = "200", description = "Профиль успешно обновлён", content = @Content(schema = @Schema(implementation = User.class))), @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")})
    @PatchMapping("/me")
    public ResponseEntity<User> updateUser(@RequestBody UpdateUser updateUser) {
        return ResponseEntity.ok(new User());
    }

    @Operation(summary = "Обновление аватара авторизованного пользователя", description = "Заменяет изображение профиля пользователя", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Форма с файлом изображения", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(type = "object", description = "Объект, содержащий файл 'image'"))), responses = {@ApiResponse(responseCode = "200", description = "Аватар успешно обновлён"), @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")})
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(@RequestPart("image") byte[] image) {
        return ResponseEntity.ok().build();
    }
}