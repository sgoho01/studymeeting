package com.ghsong.studymeeting.study;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghsong.studymeeting.account.CurrentUser;
import com.ghsong.studymeeting.domain.Account;
import com.ghsong.studymeeting.domain.Study;
import com.ghsong.studymeeting.domain.Tag;
import com.ghsong.studymeeting.domain.Zone;
import com.ghsong.studymeeting.tag.TagService;
import com.ghsong.studymeeting.tag.form.TagForm;
import com.ghsong.studymeeting.study.form.StudyDescriptionForm;
import com.ghsong.studymeeting.tag.TagRepository;
import com.ghsong.studymeeting.zone.ZoneRepository;
import com.ghsong.studymeeting.zone.ZoneService;
import com.ghsong.studymeeting.zone.form.ZoneForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : song6
 * Date: 2020-06-07
 * Copyright(©) 2020
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/study/{path}/settings")
public class StudySettingsController {

    private final StudyService studyService;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final TagRepository tagRepository;
    private final TagService tagService;
    private final ZoneRepository zoneRepository;
    private final ZoneService zoneService;


    @GetMapping("/description")
    public String viewStudySettings(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study study = studyService.getStudy(path);
        model.addAttribute(account);
        model.addAttribute(study);
        model.addAttribute(modelMapper.map(study, StudyDescriptionForm.class));
        return "study/settings/description";
    }

    @PostMapping("/description")
    public String updateStudyInfo(@CurrentUser Account account, @PathVariable String path,
                                  @Valid StudyDescriptionForm studyDescriptionForm, Errors errors,
                                  Model model, RedirectAttributes redirectAttributes) {
        Study study = studyService.getStudyToUpdate(account, path);

        if(errors.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute(study);
            return "study/settings/description";
        }

        studyService.updaetStudyDescription(study, studyDescriptionForm);
        redirectAttributes.addFlashAttribute("message", "스터디 소개를 수정했습니다.");
        return "redirect:/study/" + study.getEncodePath() + "/settings/description";
    }

    @GetMapping("/banner")
    public String viewStudyBanner(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study study = studyService.getStudy(path);
        model.addAttribute(account);
        model.addAttribute(study);
        return "study/settings/banner";
    }

    @PostMapping("/banner/enable")
    public String enableStudyBanner(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study study = studyService.getStudyToUpdate(account, path);
        studyService.enableStudyBanner(study);
        model.addAttribute(account);
        model.addAttribute(study);
        return "redirect:/study/" + study.getEncodePath() + "/settings/banner";
    }

    @PostMapping("/banner/disable")
    public String disableStudyBanner(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study study = studyService.getStudyToUpdate(account, path);
        studyService.disableStudyBanner(study);
        model.addAttribute(account);
        model.addAttribute(study);
        return "redirect:/study/" + study.getEncodePath() + "/settings/banner";
    }

    @PostMapping("/banner")
    public String updateStudyBanner(@CurrentUser Account account, @PathVariable String path,
                                    String image, Model model, RedirectAttributes redirectAttributes) {
        Study study = studyService.getStudyToUpdate(account, path);
        studyService.updateStudyBanner(study, image);
        redirectAttributes.addAttribute("message", "스터디 배너이미지가 변경되었습니다.");
        return "redirect:/study/" + study.getEncodePath() + "/settings/banner";
    }


    @GetMapping("/tags")
    public String getStudyTags(@CurrentUser Account account, @PathVariable String path, Model model) throws JsonProcessingException {
        Study study = studyService.getStudyToUpdateTag(account, path);
        model.addAttribute(account);
        model.addAttribute(study);

        model.addAttribute("tags", study.getTags().stream().map(Tag::getTitle).collect(Collectors.toList()));
        List<String> allTags = tagRepository.findAll().stream().map(Tag::getTitle).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allTags));
        return "study/settings/tags";
    }

    @PostMapping("/tags/add")
    @ResponseBody
    public ResponseEntity addStudyTags(@CurrentUser Account account, @PathVariable String path, @RequestBody TagForm tagForm) {
        Study study = studyService.getStudyToUpdateTag(account, path);
        Tag tag = tagService.findOrCreateNewTag(tagForm.getTagTitle());
        studyService.addTag(study, tag);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tags/remove")
    @ResponseBody
    public ResponseEntity removeStudyTag(@CurrentUser Account account, @PathVariable String path, @RequestBody TagForm tagForm) {
        Study study = studyService.getStudyToUpdateTag(account, path);
        Tag tag = tagRepository.findByTitle(tagForm.getTagTitle()).orElse(null);
        if (tag == null) {
            return ResponseEntity.badRequest().build();
        }
        studyService.removeTag(study, tag);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/zones")
    public String getStudyZones(@CurrentUser Account account, @PathVariable String path, Model model) throws JsonProcessingException {
        Study study = studyService.getStudyToUpdateZone(account, path);
        model.addAttribute(account);
        model.addAttribute(study);

        model.addAttribute("zones", study.getZones().stream().map(Zone::toString).collect(Collectors.toList()));
        List<String> allZones = zoneRepository.findAll().stream().map(Zone::toString).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allZones));
        return "study/settings/zones";
    }

    @PostMapping("/zones/add")
    @ResponseBody
    public ResponseEntity addStudyZone(@CurrentUser Account account, @PathVariable String path, @RequestBody ZoneForm zoneForm) {
        Study study = studyService.getStudyToUpdateZone(account, path);
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName()).orElse(null);
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }
        studyService.addZone(study, zone);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/zones/remove")
    @ResponseBody
    public ResponseEntity removeStudyZone(@CurrentUser Account account, @PathVariable String path, @RequestBody ZoneForm zoneForm) {
        Study study = studyService.getStudyToUpdateZone(account, path);
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName()).orElse(null);
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }
        studyService.removeZone(study, zone);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/study")
    public String getStudySetting(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study study = studyService.getStudyToUpdate(account, path);
        model.addAttribute(account);
        model.addAttribute(study);
        return "study/settings/study";
    }

    @PostMapping("/study/publish")
    public String publishStudy(@CurrentUser Account account, @PathVariable String path, Model model, RedirectAttributes redirectAttributes) {
        Study study = studyService.getStudyToUpdateStatus(account, path);
        studyService.publishStudy(study);
        model.addAttribute(account);
        model.addAttribute(study);
        redirectAttributes.addFlashAttribute("message", "스터디를 공개했습니다.");
        return "redirect:/study/" + study.getEncodePath() + "/settings/study";
    }


    @PostMapping("/study/close")
    public String closeStudy(@CurrentUser Account account, @PathVariable String path, Model model, RedirectAttributes redirectAttributes) {
        Study study = studyService.getStudyToUpdateStatus(account, path);
        studyService.closeStudy(study);
        model.addAttribute(account);
        model.addAttribute(study);
        redirectAttributes.addFlashAttribute("message", "스터디를 종료했습니다.");
        return "redirect:/study/" + study.getEncodePath() + "/settings/study";
    }

    @PostMapping("/study/start")
    public String startRecruit(@CurrentUser Account account, @PathVariable String path, Model model, RedirectAttributes redirectAttributes) {
        Study study = studyService.getStudyToUpdateStatus(account, path);
        model.addAttribute(account);
        model.addAttribute(study);

        if (!study.canUpdateRecruiting()) {
            redirectAttributes.addFlashAttribute("message", "1시간 안에 인원 모집 설정을 여러번 변경할 수 없습니다.");
            return "redirect:/study/" + study.getEncodePath() + "/settings/study";
        }

        studyService.startRecruit(study);
        redirectAttributes.addFlashAttribute("message", "인원 모집을 시작 합니다.");
        return "redirect:/study/" + study.getEncodePath() + "/settings/study";
    }

    @PostMapping("/study/stop")
    public String stopRecruit(@CurrentUser Account account, @PathVariable String path, Model model, RedirectAttributes redirectAttributes) {
        Study study = studyService.getStudyToUpdateStatus(account, path);
        model.addAttribute(account);
        model.addAttribute(study);

        if (!study.canUpdateRecruiting()) {
            redirectAttributes.addFlashAttribute("message", "1시간 안에 인원 모집 설정을 여러번 변경할 수 없습니다.");
            return "redirect:/study/" + study.getEncodePath() + "/settings/study";
        }

        studyService.stopRecruit(study);
        redirectAttributes.addFlashAttribute("message", "인원 모집을 중단 합니다.");
        return "redirect:/study/" + study.getEncodePath() + "/settings/study";
    }

    @PostMapping("/study/path")
    public String updateStudyPath(@CurrentUser Account account, @PathVariable String path, String newPath, Model model, RedirectAttributes redirectAttributes) {
        Study study = studyService.getStudyToUpdateStatus(account, path);
        model.addAttribute(account);
        model.addAttribute(study);

        if (!studyService.isValidPath(newPath)) {
           model.addAttribute("studyPathError", "해당 스터디 경로는 사용할 수 없습니다. 다른 경로를 입력하세요");
           return "study/settings/study";
        }

        studyService.updatePath(study, newPath);
        redirectAttributes.addFlashAttribute("message", "스터디 경로가 변경되었습니다.");
        return "redirect:/study/" + study.getEncodePath() + "/settings/study";
    }

    @PostMapping("/study/title")
    public String updateStudyTitle(@CurrentUser Account account, @PathVariable String path, String newTitle, Model model, RedirectAttributes redirectAttributes) {
        Study study = studyService.getStudyToUpdateStatus(account, path);
        model.addAttribute(account);
        model.addAttribute(study);

        if(!studyService.isValidTitle(newTitle)) {
            model.addAttribute("studyTitleError", "스터디 이름을 다시 입력하세요.");
            return "study/settings/study";
        }

        studyService.updateTitle(study, newTitle);
        redirectAttributes.addFlashAttribute("message", "스터디 이름이 변경되었습니다.");
        return "redirect:/study/" + study.getEncodePath() + "/settings/study";
    }



}
