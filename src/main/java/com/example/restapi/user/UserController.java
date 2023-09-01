package com.example.restapi.user;

import com.example.restapi.exception.BaseResponse;
import com.example.restapi.user.domain.DeleteUserReq;
import com.example.restapi.user.domain.LoginReq;
import com.example.restapi.user.domain.ModifyUserReq;
import com.example.restapi.user.domain.PostUserReq;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("api/user/{user_id}")
    public BaseResponse getUser(@PathVariable("user_id") Integer id) {
        return userService.getUser(id);
    }

    @PostMapping("api/user")
    public ResponseEntity postUser(@Valid @RequestBody PostUserReq postUserReq) {
        return userService.postUser(postUserReq);
    }

    @PatchMapping("api/user")
    public ResponseEntity patchUser(@Valid @RequestBody ModifyUserReq modifyUserReq) {
        return userService.modifyUser(modifyUserReq);
    }

    @DeleteMapping("api/user")
    public ResponseEntity deleteUser(@Valid @RequestBody DeleteUserReq deleteUserReq) {
        return userService.deleteUser(deleteUserReq);
    }

    @PostMapping("api/user/login")
    public BaseResponse login(@Valid @RequestBody LoginReq loginReq) {
        return userService.login(loginReq);
    }

    @GetMapping("api/users")
    public BaseResponse getUserlist() {
        return userService.getUserlist();
    }
}

