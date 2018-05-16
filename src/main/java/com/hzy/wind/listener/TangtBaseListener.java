package com.hzy.wind.listener;

import com.hzy.wind.base.BaseListener;
import com.hzy.wind.utils.JwtUtil;
import io.jsonwebtoken.Claims;

/**
 * Created by EduHzy-019 on 2018-05-16.
 */
public abstract class TangtBaseListener extends BaseListener {
    /**
     * 通过TOKEN获取 是否具有房间管理员权限
     * @param token
     * @return
     */
    public boolean isPowerByClaims(Claims claims){
        return claims.get("role",String.class).equals("ROOM_ADMIN");
    }

    /**
     * 通过TOKEN获取 用户名
     * @param claims
     * @return
     */
    public String getUserNameByClaims(Claims claims){
        return claims.get("unique_name",String.class);
    }

    /**
     * 通过TOKEN获取 用户ID
     * @param claims
     * @return
     */
    public Integer getUserIdByClaims(Claims claims){
        return claims.get("user_id",Integer.class);
    }

    /**
     * 通过TOKEN获取 所有TOKEN中存储字段的集合
     * @param token
     * @return
     */
    public Claims getClaimsByToken(String token){
        return JwtUtil.parseJWT(token,secretKey);
    }
}
