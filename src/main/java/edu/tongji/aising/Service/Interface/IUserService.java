package edu.tongji.aising.Service.Interface;

import edu.tongji.aising.Repository.User;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;


@Service
public interface IUserService {

    // 注册用户
    User register(String name, String password, String photo);

    // 查询用户--- 账号登录
    String accountLogin(String name, String password);
}
