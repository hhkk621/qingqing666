package com.javasm.qingqing.skill.service.impl;

import com.javasm.qingqing.common.exception.ExceptionEnum;
import com.javasm.qingqing.common.exception.JavasmException;
import com.javasm.qingqing.skill.entity.Skill;
import com.javasm.qingqing.skill.service.SkillService;
import com.javasm.qingqing.skill.vo.SkillSaveVo;
import jakarta.annotation.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @className: SkillServiceImpl
 * @author:
 * @date: 2025/12/4
 * @version: 0.1
 * @since: jdk17
 * @description: Skill业务层实现类
 */
@Service("skillService")
public class SkillServiceImpl implements SkillService {

    @Resource
    RestTemplate restTemplate;

    private static final String BASE_URL = "http://127.0.0.1:8088";

    @Override
    public List<Skill> getAllSkills() {
        String url = BASE_URL + "/skill/list";
        ResponseEntity<Skill[]> responseEntity = restTemplate.getForEntity(url, Skill[].class);
        Skill[] skills = responseEntity.getBody();
        if (skills != null) {
            return Arrays.stream(skills).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Skill getSkillById(Integer id) {
        String url = BASE_URL + "/skill/get/{id}";
        ResponseEntity<Skill> responseEntity = restTemplate.getForEntity(url, Skill.class, id);
        return responseEntity.getBody();
    }

    @Override
    public boolean addSkill(SkillSaveVo vo) {
        String url = BASE_URL + "/skill/add1";

        // 设置header的信息
        HttpHeaders headers = new HttpHeaders();
        // application/json
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SkillSaveVo> request = new HttpEntity<>(vo, headers);
        // 发起请求
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return response.getStatusCode() == HttpStatus.OK;
    }

    @Override
    public boolean updateSkill(SkillSaveVo vo) {
        String url = BASE_URL + "/skill/update1";

        // 设置header的信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 组装参数
        HttpEntity<SkillSaveVo> request = new HttpEntity<>(vo, headers);

        // 发起调用
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);

        return response.getStatusCode() == HttpStatus.OK;
    }

    @Override
    public boolean deleteSkill(Integer id) {
        String url = BASE_URL + "/skill/del/{id}";
        try {
            restTemplate.delete(url, id);
            return true;
        } catch (Exception e) {
            throw new JavasmException(ExceptionEnum.SYSTEM_ERROR);
        }
    }
}