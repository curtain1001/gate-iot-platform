package net.pingfang.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import net.pingfang.business.domain.BtpArea;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-21 11:06
 */
@Mapper
public interface BtpAreaMapper extends BaseMapper<BtpArea> {

	/**
	 * 插入场站信息
	 *
	 * @param area 信息
	 * @return 主键
	 */
	@Insert("insert into btp_area(area_id, area_name, area_no, create_by, create_time, remark)"
			+ " values(#{areaId}, #{areaName}, #{areaNo}, #{createBy}, #{createTime}, #{remark})")
	public int insertArea(BtpArea area);

	/**
	 * 根据场站号查询场站信息
	 *
	 * @param areaNo 场站号
	 * @return 场站信息
	 */
	@Select("select * from btp_area where area_no = #{areaNo}")
	public BtpArea selectByAreaNo(String areaNo);

	/**
	 * 查询场站列表
	 *
	 * @param area 场站信息
	 * @return 场站信息集合
	 */
	@Select("<script>select * from btp_area " //
			+ "<where>" //
			+ "<if test=\"areaName!=null\">" //
			+ " and area_name = #{areaName} " //
			+ "</if>" //
			+ "<if test=\"areaNo!=null\">" //
			+ " and area_no = #{areaNo} " //
			+ "</if>" //
			+ "</where>" //
			+ "</script>")
	public List<BtpArea> selectAreaList(BtpArea area);

	/**
	 * 查询场站信息唯一
	 *
	 * @param areaNo 场站单一属性
	 * @return 场站信息
	 */
	@Select("select * from btp_area where area_no=#{areaNo} limit 1")
	public BtpArea checkAreaNoUnique(String areaNo);

	/**
	 * 查询场站信息唯一
	 *
	 * @param areaName 场站单一属性
	 * @return 场站信息
	 */
	@Select("select * from btp_area where area_name=#{areaName} limit 1")
	public BtpArea checkAreaNameUnique(String areaName);

	/**
	 * 修改参数配置
	 *
	 * @param area 参数配置信息
	 * @return 结果
	 */
	@Update("update btp_area set area_name = #{areaName},area_no = #{areaNo},update_by = #{updateBy},update_time = sysdate() where area_id = #{areaId}")
	public int updateArea(BtpArea area);

	/**
	 * 删除参数配置
	 *
	 * @param areaId 参数ID
	 * @return 结果
	 */
	@Delete("delete form btp_area where area_id = #{areaId}")
	public int deleteAreaById(Long areaId);

	/**
	 * 批量删除参数信息
	 *
	 * @param areaIds 需要删除的参数ID
	 * @return 结果
	 */
	@Delete("<script>delete from btp_area where area_id in "//
			+ "<foreach close=\")\" collection=\"array\" item=\"areaIds\" open=\"(\" separator=\",\"> "//
			+ " #{areaId} " //
			+ "</foreach>"//
			+ "</script>") //
	public int deleteAreaByIds(Long[] areaIds);

}
