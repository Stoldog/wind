package com.hzy.wind.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.hzy.wind.base.BaseListener;
import com.hzy.wind.base.entity.BasePacket;
import com.hzy.wind.type.Event;
import com.hzy.wind.type.MesType;
import com.hzy.wind.type.Role;
import com.hzy.wind.utils.JwtUtil;
import io.jsonwebtoken.Claims;

/**
 * Created by EduHzy-019 on 2018-05-16.
 * 此抽象类为堂堂网项目专属监听器类 包含了一些通用的方法
 */
public abstract class TangtBaseListener extends BaseListener {
    /**
     * 通过TOKEN获取 是否具有房间管理员权限
     * @param claims
     * @return
     */
    public boolean isPowerByClaims(Claims claims){
        return claims.get("role",String.class).equals("ROOM_ADMIN");
    }

    /**
     * 通过TOKEN获取 是否具有房间管理员权限 如果没有权限，则向客户端发送一条系统消息
     * @param claims
     * @return
     */
    public boolean checkIsPowerAndSend(SocketIOClient client, Claims claims){
        boolean isPower = claims.get("role",String.class).equals(Role.ROOM_ADMIN.getName());
        if(!isPower){
            client.sendEvent(Event.ERROR.getName(),new BasePacket(MesType.ERROR,"您没有此操作权限！","系统",-1));
        }
        return isPower;
    }

    /**
     * 通过TOKEN获取 是否被禁言 如果没有权限，则向客户端发送一条系统消息
     * @param client
     * @param claims
     * @return
     */
    public boolean checkSilenceAndSend(SocketIOClient client, Claims claims){
        boolean isSilence = claims.get("role",String.class).equals(Role.SILENT_MAN.getName());
        if(isSilence){
            client.sendEvent(Event.ERROR.getName(),new BasePacket(MesType.ERROR,"您已被禁言！","系统",-1));
        }
        return isSilence;
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
     * 通过TOKEN获取 Role
     * @param claims
     * @return
     */
    public Role getRoleByClaims(Claims claims){
        return Role.getByName(claims.get("role",String.class));
    }

    /**
     * 通过TOKEN获取 RoomId
     * @param claims
     * @return
     */
    public Long getRoomIdByClaims(Claims claims){
        return claims.get("room_id",Long.class);
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
