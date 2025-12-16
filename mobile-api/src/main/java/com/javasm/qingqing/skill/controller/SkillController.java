package com.javasm.qingqing.skill.controller;

import com.javasm.qingqing.common.exception.R;
import com.javasm.qingqing.common.utils.JWTUtil;
import com.javasm.qingqing.skill.entity.Skill;
import com.javasm.qingqing.skill.service.SkillService;
import com.javasm.qingqing.skill.vo.SkillSaveVo;
import jakarta.annotation.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @className: SkillController
 * @author: gfs
 * @date: 2025/12/3 14:41
 * @version: 0.1
 * @since: jdk17
 * @description: 技能模块控制器
 */
@RestController
@RequestMapping("/skill")
public class SkillController {

    @Resource
    RestTemplate restTemplate;
    
    @Resource
    SkillService skillService;

    @GetMapping("/all/list")
    public R queryList(){
        List<Skill> skills = skillService.getAllSkills();
        return R.ok(skills);
    }

    @GetMapping("/{id}")
    public R getSkillById(@PathVariable Integer id) {
        Skill skill = skillService.getSkillById(id);
        return R.ok(skill);
    }

    @PostMapping("/save")
    public R save(@RequestBody SkillSaveVo vo){
        boolean result = skillService.addSkill(vo);
        if (result) {
            return R.ok();
        } else {
            return R.error("添加失败");
        }
    }
    
    @PutMapping("/update")
    public R update(@RequestBody SkillSaveVo vo){
        boolean result = skillService.updateSkill(vo);
        if (result) {
            return R.ok();
        } else {
            return R.error("更新失败");
        }
    }

    @DeleteMapping("/del/{id}")
    public R delById(@PathVariable Integer id){
        boolean result = skillService.deleteSkill(id);
        if (result) {
            return R.ok();
        } else {
            return R.error("删除失败");
        }
    }

    // 保留原有的方法以供参考
    @GetMapping("/all/list/remote")
    public R queryListFromRemote(){
        String url = "http://127.0.0.1:8088/skill/list";
        //相当于浏览器获取到了Response对象
        ResponseEntity<R> rResponseEntity = restTemplate.getForEntity(url, R.class);
        //获取body信息
        R body = rResponseEntity.getBody();
        //获取头信息
        HttpHeaders headers = rResponseEntity.getHeaders();

        return body;
    }

    @GetMapping("/save/{name}/{price}")
    public R save(@PathVariable String name,@PathVariable Double price){
        //普通参数，表单？形式
        String url = "http://127.0.0.1:8088/skill/add2";
        //拼装参数
        MultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("gameUsername",name);
        map.add("skillPrice",price);
        //设置 header对象
        HttpHeaders headers = new HttpHeaders();
        //application/x-www-form-urlencoded  表单提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //设置Request对象
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        //发起请求
        ResponseEntity<R> rResponseEntity = restTemplate.postForEntity(url, request, R.class);
        return rResponseEntity.getBody();
    }
    
    @GetMapping("/save1/{name}/{price}")
    public R save1(@PathVariable String name,@PathVariable Double price){
        //普通参数，表单？形式
        String url = "http://127.0.0.1:8088/skill/add1";
        //拼装参数
        SkillSaveVo vo = new SkillSaveVo();
        vo.setGameUsername(name);
        vo.setSkillPrice(price);
        //设置header的信息
        HttpHeaders headers = new HttpHeaders();
        //application/json
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SkillSaveVo> request = new HttpEntity<>(vo, headers);
        //发起请求
        R r = restTemplate.postForObject(url, request, R.class);
        return r;
    }

    @GetMapping("/update/remote")
    public R updateRemote(Integer id,String name){
        String url = "http://127.0.0.1:8088/skill/update2";
        MultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("skillId",id);
        map.add("gameUsername",name);

        restTemplate.put(url,map);
        return R.ok();
    }

    @GetMapping("/f1")
    public R f1(Integer id,String name){
        String url = "http://127.0.0.1:8088/skill/update1";
        //组装参数
        SkillSaveVo vo = new SkillSaveVo();
        vo.setSkillId(id);
        vo.setGameUsername(name);
        //header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //生成Token
        String token = JWTUtil.generateUid(id.toString());
        headers.add(JWTUtil.Server_Token,token);
        //body
        HttpEntity<SkillSaveVo> body = new HttpEntity<>(vo, headers);
        //发起调用
        ResponseEntity<R> exchange = restTemplate.exchange(url, HttpMethod.PUT, body, R.class);

        return exchange.getBody();
    }

}