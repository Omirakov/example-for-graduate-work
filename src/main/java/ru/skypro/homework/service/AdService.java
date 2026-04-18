package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;

    public Ads getAllAds() {
        List<Ad> ads = adRepository.findAll().stream().map(adMapper::toDto).collect(Collectors.toList());
        Ads result = new Ads();
        result.setCount(ads.size());
        result.setResults(ads);
        return result;
    }

    public ExtendedAd getExtendedAd(Integer id) {
        AdEntity adEntity = adRepository.getReferenceById(id);
        return adMapper.toExtendedDto(adEntity);
    }

    public Ad createAd(AdEntity adEntity) {
        AdEntity saved = adRepository.save(adEntity);
        return adMapper.toDto(saved);
    }

    public void deleteAd(Integer id) {
        adRepository.deleteById(id);
    }

    public Ad updateAd(Integer id, AdEntity updatedAd) {
        updatedAd.setPk(id);
        AdEntity saved = adRepository.save(updatedAd);
        return adMapper.toDto(saved);
    }

    public Ads getAdsByUser(Integer userId) {
        List<Ad> ads = adRepository.findByAuthorId(userId).stream().map(adMapper::toDto).collect(Collectors.toList());
        Ads result = new Ads();
        result.setCount(ads.size());
        result.setResults(ads);
        return result;
    }

    public Ad updateImage(Integer id, byte[] image) {
        AdEntity adEntity = adRepository.getReferenceById(id);
        return adMapper.toDto(adEntity);
    }
}