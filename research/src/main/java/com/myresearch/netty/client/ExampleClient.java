package com.myresearch.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Date;

import com.sun.corba.se.impl.ior.ByteBuffer;

public class ExampleClient {
 
      public static void main(String args[]) {
           // Client服务启动器 3.x的ClientBootstrap 改为Bootstrap，且构造函数变化很大，这里用无参构造。
          Bootstrap bootstrap = new Bootstrap();
           // 指定channel类型
          bootstrap.channel(NioSocketChannel.class );
           // 指定Handler
          bootstrap.handler(new HelloClientHandler());
           // 指定EventLoopGroup
          bootstrap.group(new NioEventLoopGroup());
           // 连接到本地的8000端口的服务端
          bootstrap.connect( new InetSocketAddress("127.0.0.1" , 8000));
     }
 
      private static class HelloClientHandler extends
              ChannelInboundHandlerAdapter {
    	  	ByteBuffer bb = new ByteBuffer();
    	    @Override
    	    public void channelActive(ChannelHandlerContext ctx) {
    	       System.out.println("channelActive");
    	    }

    	    @Override
    	    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	        ByteBuf m = (ByteBuf) msg; // (1)
    	        try {
    	            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
    	            System.out.println(new Date(currentTimeMillis));
    	            ctx.close();
    	        } finally {
    	            m.release();
    	        }
    	    }

    	    @Override
    	    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    	    	ctx.flush();
    	    }

    	    @Override
    	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	    	System.out.println(cause);
    	        ctx.close();
    	    }
     }
}