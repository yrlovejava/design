package com.book.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.book.pojo.Order;
import com.book.service.OrderService;
import com.book.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单控制层
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Order createOrder(@RequestParam String productId){
        return orderService.createOrder(productId);
    }

    @PostMapping("/pay")
    public String payOrder(@RequestParam String orderId,
                          @RequestParam Float price,
                          @RequestParam Integer payType){
        return orderService.getPayUrl(orderId,price,payType);
    }

    @PostMapping("/send")
    public Order send(@RequestParam String orderId){
        return orderService.send(orderId);
    }

    @PostMapping("/receive")
    public Order receive(@RequestParam String orderId){
        return orderService.receive(orderId);
    }

    @RequestMapping("/alipayCallback")
    public String alipayCallback(HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException {
        // 获取回调信息
        Map<String,String> params = new HashMap<>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(name, valueStr);
        }
        // 验证签名，确定回调接口真的是支付宝平台触发的
        boolean signVerified = AlipaySignature.rsaCertCheckV1(params, Constants.ALIPAY_PUBLIC_KEY, String.valueOf(StandardCharsets.UTF_8), Constants.SIGN_TYPE);
        // 确认是支付宝发起的回调
        if(signVerified){
            // 商户订单号
            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 支付金额
            float totalAmount = Float.parseFloat(new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            // 进行相关的业务处理
            Order order = orderService.pay(outTradeNo);
            return "支付成功页面跳转, 当前订单为：" + order;
        } else {
            throw new UnsupportedEncodingException("callback verify failed");
        }
    }
}
