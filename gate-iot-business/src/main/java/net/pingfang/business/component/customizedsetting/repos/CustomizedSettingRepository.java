//package net.pingfang.business.component.customizedsetting.repos;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//import com.google.common.collect.Lists;
//
//import cn.zhijian.customizedsetting.mappers.CustomizedSettingMapper;
//import cn.zhijian.customizedsetting.mappers.rows.CustomizedSettingRow;
//import cn.zhijian.customizedsetting.values.AttributeEnum;
//import cn.zhijian.customizedsetting.values.CustomizedSettingData;
//import cn.zhijian.customizedsetting.values.Dosage;
//import cn.zhijian.utils.JsonUtils;
//import lombok.AllArgsConstructor;
//
//@AllArgsConstructor
//public class CustomizedSettingRepository {
//	final CustomizedSettingMapper mapper;
//
//	public CustomizedSettingData load(String id) {
//		return covered(mapper.load(id));
//	}
//
//	public CustomizedSettingData findByAttribute(String corpId, AttributeEnum attributeEnum) {
//		return covered(mapper.findByAttribute(corpId, attributeEnum)) == null
//				? CustomizedSettingData.builder().attribute(attributeEnum)
//						.customizeType(attributeEnum.getCustomizeType())
//						.dosage(Dosage.builder().label(attributeEnum.getLabel()).type(attributeEnum.getType())
//								.value(attributeEnum.getDefaults()).build())
//						.build()
//				: covered(mapper.findByAttribute(corpId, attributeEnum));
//	}
//
//	public List<CustomizedSettingData> findByCorpId(String corpId) {
//		List<CustomizedSettingRow> rows = mapper.findByCorpId(corpId);
//		if (rows != null && !rows.isEmpty()) {
//			return rows.stream().map(this::covered).collect(Collectors.toList());
//		} else {
//			return Lists.newArrayList();
//		}
//	}
//
//	public List<CustomizedSettingData> findByCustomizeType(String corpId, String customizeType) {
//		List<CustomizedSettingRow> rows = mapper.findByCustomizeType(corpId, customizeType);
//		if (rows != null && !rows.isEmpty()) {
//			return rows.stream().map(this::covered).collect(Collectors.toList());
//		} else {
//			return Lists.newArrayList();
//		}
//	}
//
//	public List<CustomizedSettingData> list(String corpId) {
//		List<CustomizedSettingRow> row = mapper.list(corpId);
//		Map<Object, CustomizedSettingData> enums = row.stream().filter(Objects::nonNull)
//				.collect(Collectors.toMap(x -> x.getAttribute().getValue(), this::covered));
//		return this.getSettings(enums);
//	}
//
//	private List<CustomizedSettingData> getSettings(Map<Object, CustomizedSettingData> existRows) {
//		return Arrays.stream(AttributeEnum.values()).map(x -> {
//			return existRows != null && existRows.get(x.getValue()) != null ? existRows.get(x.getValue())
//					: CustomizedSettingData.builder() //
//							.customizeType(x.getCustomizeType())//
//							.attribute(x) //
//							.dosage(Dosage.builder() //
//									.type(x.getType()) //
//									.label(x.getLabel()) //
//									.value(x.getDefaults()) //
//									.build()) //
//							.build();
//		}).collect(Collectors.toList());
//	}
//
//	private CustomizedSettingData covered(CustomizedSettingRow row) {
//		if (row == null) {
//			return null;
//		}
//		return CustomizedSettingData.builder() //
//				.customizeType(row.getAttribute().getCustomizeType())//
//				.attribute(row.getAttribute()) //
//				.dosage(JsonUtils.toObject(row.getSettingData(), Dosage.class)) //
//				.id(row.getId()) //
//				.build(); //
//	}
//}
