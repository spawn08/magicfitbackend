package com.spawnai.middleware.controllers;


import com.spawnai.middleware.interfaces.ModelRespository;
import com.spawnai.middleware.interfaces.UserRepository;
import com.spawnai.middleware.models.ModelConfiguration;
import com.spawnai.middleware.models.User;
import com.spawnai.middleware.utils.WebApplicationUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class SpawnBotRestController {

    private Logger LOGGER = LoggerFactory.getLogger(SpawnBotRestController.class.getName());

    @Autowired
    private ModelRespository modelRespository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/api/user/{id}")
    public Object product(@PathVariable String id) {
        try {
            ModelConfiguration existModelConfig = modelRespository.findBy_id(id);

            if (existModelConfig != null) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("token", existModelConfig.getToken());
                map.put("modelName", existModelConfig.getModelName());
                map.put("username", existModelConfig.getUsername());
                map.put("project", existModelConfig.getProject());
                return ok(map);
            } else {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("token", "");
                map.put("modelName", "");
                map.put("username", "");
                return ok(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            HashMap<String, String> map = WebApplicationUtils.getInstance()
                    .getErrorMessage("unauthorized access", "Invalid access", "failure");
            return ok(map);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/user")
    public Object modelConfiguration(HttpServletRequest request, @RequestBody ModelConfiguration modelConfiguration) {
        try {
            ModelConfiguration existModelConfig = modelRespository.findByEmailId(modelConfiguration.getEmailId());
            User user = userRepository.findByEmail(modelConfiguration.getEmailId());
            String header = request.getHeader("Authorization");
            header = header.replaceAll("Bearer ", "");

            if (existModelConfig != null && user != null && user.getToken().equals(header)) {
                if (existModelConfig.getToken() == null) {
                    existModelConfig.setToken(WebApplicationUtils.getInstance().generateId());
                    modelRespository.save(existModelConfig);
                } else if (existModelConfig.getUsername() == null) {
                    existModelConfig.setUsername(modelConfiguration.getUsername());
                    modelRespository.save(existModelConfig);
                } else if (existModelConfig.getModelName() == null) {
                    existModelConfig.setModelName(modelConfiguration.getModelName());
                    modelRespository.save(existModelConfig);
                } else if (existModelConfig.getProject() == null) {
                    existModelConfig.setProject(modelConfiguration.getProject());
                    modelRespository.save(existModelConfig);
                }

                HashMap<String, String> map = new HashMap<>();
                map.put("modelName", existModelConfig.getModelName());
                map.put("token", existModelConfig.getToken());
                map.put("id", existModelConfig.getId());
                map.put("project", existModelConfig.getProject());
                return ok(map);
            } else if (user != null
                    && user.getToken() != null
                    && user.getToken().equals(header)) {
                modelConfiguration.setToken(WebApplicationUtils.getInstance().generateId());
                modelConfiguration.setId(ObjectId.get().toHexString());
                modelRespository.save(modelConfiguration);
                HashMap<String, String> map = new HashMap<>();
                map.put("modelName", modelConfiguration.getModelName());
                map.put("token", modelConfiguration.getToken());
                map.put("id", modelConfiguration.getId());
                map.put("project", modelConfiguration.getProject());
                return ok(map);
            } else {
                HashMap<String, String> map = WebApplicationUtils.getInstance()
                        .getErrorMessage("unauthorized access", "Invalid access", "failure");
                return ok(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            HashMap<String, String> map = WebApplicationUtils.getInstance()
                    .getErrorMessage("unauthorized access", "Invalid access", "failure");
            return ok(map);
        }


    }

}
