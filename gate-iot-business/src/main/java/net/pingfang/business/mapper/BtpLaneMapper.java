package net.pingfang.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import net.pingfang.business.domain.BtpLane;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-21 15:57
 */
@Mapper
public interface BtpLaneMapper extends BaseMapper<BtpLane> {

	@Select("select * from btp_lane where area_id = #{areaId}")
	public List<BtpLane> getLaneByAreaId(Long areaId);
}
