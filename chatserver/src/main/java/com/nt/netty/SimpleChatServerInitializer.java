package com.nt.netty;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 服务端 ChannelInitializer
 * 
 * @author waylau.com
 * @date 2015-2-26
 */
public class SimpleChatServerInitializer extends
		ChannelInitializer<SocketChannel> {

	@Override
    public void initChannel(SocketChannel ch) throws Exception {
		 ChannelPipeline pipeline = ch.pipeline();
		 pipeline.addLast(new ProtobufVarint32FrameDecoder());
         // 添加ProtobufDecoder解码器，它的参数是com.google.protobuf.MessageLite
         // 实际上就是要告诉ProtobufDecoder需要解码的目标类是什么，否则仅仅从字节数组中是
         // 无法判断出要解码的目标类型信息的（服务端需要解析的是客户端请求，所以是Req）
		 pipeline.addLast(new ProtobufDecoder(SubscribeReqProto.SubscribeReq.getDefaultInstance()));
		  ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
          // 添加ProtobufEncoder编码器，这样就不需要对SubscribeResp进行手工编码
          ch.pipeline().addLast(new ProtobufEncoder());
         pipeline.addLast("handler", new SimpleChatServerHandler());
		System.out.println("SimpleChatClient:"+ch.remoteAddress() +"连接上");
    }
}
