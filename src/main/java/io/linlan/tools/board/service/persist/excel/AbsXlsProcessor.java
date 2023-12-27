package io.linlan.tools.board.service.persist.excel;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 
 * Filename:AbsXlsProcessor.java
 * Desc:
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday soo</a>
 * CreateTime:2018/1/3 12:04
 *
 * @version 1.0
 * @since 1.0
 *
 */
public abstract class AbsXlsProcessor {

    private static int DEFAULT_HEIGHT = 18;
    public ClientAnchor draw(XlsProcessor context) {
        Row row = context.getBoardSheet().getRow(context.getR1());
        if (row == null) {
            row = context.getBoardSheet().createRow(context.getR1());
        }
        JSONObject widgetJson = context.getWidget();
        Cell cell = row.createCell(context.getC1());
        cell.setCellValue(widgetJson.getString("name"));
        cell.setCellStyle(context.getTitleStyle());
        context.getBoardSheet().addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cell.getColumnIndex(), context.getC2()));
        row = context.getBoardSheet().createRow(context.getR1() + 1);
        row.setHeight((short) 130);
        int r1 = context.getR1() + 2;
        int r2 = r1 + DEFAULT_HEIGHT;
        Integer wightHeight = widgetJson.getInteger("height");
        if (wightHeight != null) {
            r2 = r1 + DEFAULT_HEIGHT * wightHeight / 450;
        }
        context.setR1(context.getR1() + 2);
        context.setR2(r2);
        return drawContent(context);
    }

    protected abstract ClientAnchor drawContent(XlsProcessor context);
}
