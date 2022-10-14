package net.pingfang.dockservice.instruction;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.utils.http.HttpUtils;
import net.pingfang.dockservice.DockProduct;
import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.instruction.InstructionType;
import net.pingfang.iot.common.product.Product;
import net.pingfang.servicecomponent.core.BusinessInstruction;

/**
 * <p>
 * 流程开始指令（来车报文发送）
 * </p>
 *
 * @author 王超
 * @since 2022-10-10 10:58
 */
@Slf4j
public class ProcessStartsInstruction  implements BusinessInstruction {


    @Override
    public String getName() {
        return "流程开始";
    }

    @Override
    public String getValue() {
        return "PROCESS_STARTS";
    }

    @Override
    public InstructionType getInsType() {
        return InstructionType.down;
    }

    @Override
    public Product getProduct() {
        return DockProduct.DOCK_SERVICE;
    }

    @Override
    public InstructionResult<String, String> execution(JsonNode jsonNode) {
        log.info("流程开始{}", jsonNode);
//        HttpUtils.sendPost()
        return InstructionResult.success(null, "执行成功");
    }


}
