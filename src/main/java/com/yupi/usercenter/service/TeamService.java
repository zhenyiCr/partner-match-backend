package com.yupi.usercenter.service;

import com.yupi.usercenter.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.usercenter.model.domain.User;
import com.yupi.usercenter.model.dto.TeamQuery;
import com.yupi.usercenter.model.request.TeamJoinRequest;
import com.yupi.usercenter.model.request.TeamQuitRequest;
import com.yupi.usercenter.model.request.TeamUpdateRequest;
import com.yupi.usercenter.model.vo.TeamUserVO;

import java.util.List;

/**
*
*/
public interface TeamService extends IService<Team> {

    /**
     * 添加队伍
     * @param team
     * @return 队伍id
     */
    long addTeam(Team team, User loginUser);

    /**
     * 查询队伍列表
     * @param teamQuery
     * @return 队伍列表
     */
    List<TeamUserVO> listTeam(TeamQuery teamQuery,Boolean isAdmin);

    /**
     * 更新队伍
     * @param teamUpdateRequest
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

     /**
      * 退出队伍
      * @param teamQuitRequest
      * @return
      */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 解散队伍
     * @param id
     * @return
     */
    boolean deleteTeam(Long id, User loginUser);
}
