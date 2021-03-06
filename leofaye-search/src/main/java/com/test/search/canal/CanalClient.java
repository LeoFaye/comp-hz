package com.test.search.canal;

import java.net.InetSocketAddress;
import java.util.List;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.Message;

/**
 * canal客户端
 * @author LeoHe
 * @date 2017年12月20日 下午4:56:40
 */
public class CanalClient implements Runnable {
	
	/**
	 * @author LeoHe
	 * @date 2017年12月21日 上午10:44:53
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// 创建链接
	    CanalConnector connector = CanalConnectors
	    		.newSingleConnector(new InetSocketAddress(AddressUtils.getHostIp(), 11111), "example", "", "");
	    int batchSize = 1000;
	    int emptyCount = 0;
	    try {
	        connector.connect();
	        connector.subscribe(".*\\..*");
	        connector.rollback();
	        int totalEmptyCount = 120;
	        while (emptyCount < totalEmptyCount) {
	            Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
	            long batchId = message.getId();
	            int size = message.getEntries().size();
	            if (batchId == -1 || size == 0) {
	                emptyCount++;
	                System.out.println("empty count : " + emptyCount);
	                //System.out.println("----------现在是什么都没有获取啊--------");
	                /*try {
	                    Thread.sleep(1000);
	                } catch (InterruptedException e) {
	                }*/
	            } else {
	                emptyCount = 0;
	                // System.out.printf("message[batchId=%s,size=%s] \n", batchId, size);
	                printEntry(message.getEntries());
	            }

	            connector.ack(batchId); // 提交确认
	            // connector.rollback(batchId); // 处理失败, 回滚数据
	        }

	        System.out.println("empty too many times, exit");
	    } finally {
	        connector.disconnect();
	    }
		
	}
	
	private static void printEntry(List<Entry> entrys) {
	    for (Entry entry : entrys) {
	        if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
	            continue;
	        }

	        RowChange rowChage = null;
	        try {
	            rowChage = RowChange.parseFrom(entry.getStoreValue());
	        } catch (Exception e) {
	            throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
	                                       e);
	        }

	        EventType eventType = rowChage.getEventType();
	        System.out.println(String.format("================> binlog[%s:%s] , name[%s,%s] , eventType : %s",
	                                         entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
	                                         entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
	                                         eventType));

	        for (RowData rowData : rowChage.getRowDatasList()) {
	            if (eventType == EventType.DELETE) {
	                printColumn(rowData.getBeforeColumnsList());
	            } else if (eventType == EventType.INSERT) {
	                printColumn(rowData.getAfterColumnsList());
	            } else {
	                System.out.println("-------&gt; before");
	                printColumn(rowData.getBeforeColumnsList());
	                System.out.println("-------&gt; after");
	                printColumn(rowData.getAfterColumnsList());
	            }
	        }
	    }
	}
	private static void printColumn(List<Column> columns) {
	    for (Column column : columns) {
	        System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
	    }
	}
}
