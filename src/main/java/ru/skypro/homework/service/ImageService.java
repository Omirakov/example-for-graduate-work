package ru.skypro.homework.service;

import java.io.IOException;

public interface ImageService {
    void saveAdImage(Integer adId, byte[] imageBytes) throws IOException;

    void saveUserImage(Integer userId, byte[] imageBytes) throws IOException;

    byte[] getAdImage(Integer adId) throws IOException;

    byte[] getUserImage(Integer userId) throws IOException;
}