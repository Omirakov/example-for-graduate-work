package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;
    private final ImageService imageService;

    @Override
    public Ads getAllAds() {
        List<Ad> ads = adRepository.findAll().stream().map(adMapper::toDto).collect(Collectors.toList());

        Ads result = new Ads();
        result.setCount(ads.size());
        result.setResults(ads);
        return result;
    }

    @Override
    public ExtendedAd getExtendedAd(Integer id) {
        AdEntity ad = adRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));
        return adMapper.toExtendedDto(ad);
    }

    @Override
    public Ad createAd(AdEntity adEntity, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        adEntity.setAuthor(user);
        AdEntity saved = adRepository.save(adEntity);
        return adMapper.toDto(saved);
    }

    @Override
    public void deleteAd(Integer id, String email) {
        AdEntity ad = adRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        if (!ad.getAuthor().getId().equals(user.getId()) && !user.getRole().name().equals("ADMIN")) {
            throw new SecurityException("У вас нет прав на удаление этого объявления");
        }

        adRepository.deleteById(id);
    }

    @Override
    public Ad updateAd(Integer id, AdEntity updatedAd, String email) {
        AdEntity existing = adRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        if (!existing.getAuthor().getId().equals(user.getId()) && !user.getRole().name().equals("ADMIN")) {
            throw new SecurityException("У вас нет прав на редактирование этого объявления");
        }

        existing.setTitle(updatedAd.getTitle());
        existing.setPrice(updatedAd.getPrice());
        existing.setDescription(updatedAd.getDescription());
        AdEntity saved = adRepository.save(existing);

        return adMapper.toDto(saved);
    }

    @Override
    public Ads getAdsByUser(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        List<Ad> ads = adRepository.findByAuthorId(user.getId()).stream().map(adMapper::toDto).collect(Collectors.toList());

        Ads result = new Ads();
        result.setCount(ads.size());
        result.setResults(ads);
        return result;
    }

    @Override
    public Ad updateImage(Integer id, byte[] image, String email) throws IOException {
        AdEntity ad = adRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        if (!ad.getAuthor().getId().equals(user.getId()) && !user.getRole().name().equals("ADMIN")) {
            throw new SecurityException("Нет прав на обновление изображения");
        }

        imageService.saveAdImage(ad.getPk(), image);
        Ad result = adMapper.toDto(ad);
        result.setImage("/image/ad/" + ad.getPk());
        return result;
    }
}