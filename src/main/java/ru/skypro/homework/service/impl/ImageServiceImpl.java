package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.service.ImageService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${image.folder.path:/uploads/images}")
    private String imagePath;

    private Path imageDir;

    @PostConstruct
    public void init() throws IOException {
        imageDir = Paths.get(imagePath);
        Files.createDirectories(imageDir);
    }

    @Override
    public void saveAdImage(Integer adId, byte[] imageBytes) throws IOException {
        Path path = imageDir.resolve("ad_" + adId + ".jpg");
        Files.write(path, imageBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public void saveUserImage(Integer userId, byte[] imageBytes) throws IOException {
        Path path = imageDir.resolve("user_" + userId + ".jpg");
        Files.write(path, imageBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public byte[] getAdImage(Integer adId) throws IOException {
        Path path = imageDir.resolve("ad_" + adId + ".jpg");
        if (!Files.exists(path)) {
            path = imageDir.resolve("ad_default.jpg");
            if (!Files.exists(path)) {
                throw new EntityNotFoundException("Изображение объявления не найдено");
            }
        }
        return Files.readAllBytes(path);
    }

    @Override
    public byte[] getUserImage(Integer userId) throws IOException {
        Path path = imageDir.resolve("user_" + userId + ".jpg");
        if (!Files.exists(path)) {
            path = imageDir.resolve("user_default.jpg");
            if (!Files.exists(path)) {
                throw new EntityNotFoundException("Аватар пользователя не найден");
            }
        }
        return Files.readAllBytes(path);
    }
}