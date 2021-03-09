package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.WordsManageMapper;
import com.cxylm.springboot.dto.form.WordsSelectionForm;
import com.cxylm.springboot.exception.AppBadRequestException;
import com.cxylm.springboot.exception.AppBizException;
import com.cxylm.springboot.model.BookInfo;
import com.cxylm.springboot.model.Words;
import com.cxylm.springboot.service.WordsManageService;
import com.cxylm.springboot.util.ExcelImportUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WordsManageServiceImpl extends ServiceImpl<WordsManageMapper, Words> implements WordsManageService {

    /**
     * 批量导入词库
     *
     * @param fileName 文件名
     * @param file
     * @param bookInfo
     * @return
     */
    @Override
    public boolean batchImport(String fileName, MultipartFile file, BookInfo bookInfo) {
        String dir = System.getProperty("user.dir");
        File uploadDir = new File(dir);
        //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
//        if (!uploadDir.exists()) uploadDir.mkdirs();
        //新建一个文件
        File tempFile = new File(dir + "\\" + new Date().getTime() + ".xlsx");
        //初始化输入流
        InputStream is = null;
        try {
            //将上传的文件写入新建的文件中
            file.transferTo(tempFile);

            //根据新建的文件实例化输入流
            is = new FileInputStream(tempFile);

            //根据版本选择创建Workbook的方式
            Workbook wb = null;
            //根据文件名判断文件是2003版本还是2007版本
            if (ExcelImportUtils.isExcel2007(fileName)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = new HSSFWorkbook(is);
            }
            //根据excel里面的内容读取知识库信息
            return excelTranMysql(wb, tempFile, bookInfo);
        } catch (IOException e) {
            log.error("导入课程失败", e);
            throw new AppBizException("课程导入失败，请联系管理员");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }

            //删除上传的临时文件
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    private boolean excelTranMysql(Workbook wb, File tempFile, BookInfo bookInfo) {
        //1、先插入课本信息表
        boolean b = bookInfo.insert();
        if (!b) {
            throw new AppBizException("课本信息处理异常！");
        }

        List<Words> wordsList = readExcelValue(wb, tempFile, bookInfo.getId());

        b = saveBatch(wordsList);
        if (!b) {
            throw new AppBizException("单词信息处理异常！");
        }
        return true;
    }

    /**
     * 解析Excel里面的数据
     *
     * @param wb
     * @param tempFile
     * @param bookId   课本id
     * @return
     */
    private List<Words> readExcelValue(Workbook wb, File tempFile, Integer bookId) {
        //错误信息接收器
        StringBuilder errorMsg = new StringBuilder();
        //得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        //得到Excel的行数
        int totalRows = sheet.getPhysicalNumberOfRows();
        //总列数
        int totalCells = 0;
        //得到Excel的列数(前提是有行数)，从第二行算起
        if (totalRows >= 2 && sheet.getRow(1) != null) {
            totalCells = sheet.getRow(1).getPhysicalNumberOfCells();
        }
        if (totalCells < 3) {
            throw new AppBadRequestException("文件格式错误！");
        }
        totalCells = 3;
        List<Words> wordsList = new ArrayList<>();
        Words words;

        String br = "<br/>";

        //循环Excel行数,从第二行开始。标题不入库
        for (int r = 1; r < totalRows; r++) {
            StringBuilder rowMessage = new StringBuilder();
            Row row = sheet.getRow(r);
            if (row == null) {
//                errorMsg += br + "第" + (r + 1) + "行数据有问题，请仔细检查！";
                log.info("第" + (r + 1) + "行数据为空，已跳过！");
                break;
            }
            words = new Words();
            words.setBookId(bookId);
            //Excel数据暂时存储变量
            Double unit;
            String word;
            String chinese;
            //循环Excel的列
            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 0) {
                        unit = cell.getNumericCellValue();
                        words.setUnit(unit.intValue());
                    } else if (c == 1) {
                        word = cell.getStringCellValue();
                        if (StringUtils.isEmpty(word)) {
                            rowMessage.append("数据不能为空；");
                        } else if (word.length() > 60) {
                            rowMessage.append("字数不能超过60；");
                        }

                        words.setWord(word);
                    } else if (c == 2) {
                        chinese = cell.getStringCellValue();
                        if (StringUtils.isEmpty(chinese)) {
                            rowMessage.append("数据不能为空；");
                        } else if (chinese.length() > 200) {
                            rowMessage.append("字数不能超过200；");
                        }

                        words.setMean(chinese);
                    }
                } else {
                    rowMessage.append("第").append(c + 1).append("列数据有问题，请仔细检查；");
                }
            }
            //拼接每行的错误提示
            if (!StringUtils.isEmpty(rowMessage.toString())) {
                errorMsg.append(br).append("第").append(r + 1).append("行，").append(rowMessage);
            } else {
                wordsList.add(words);
            }
        }

        //全部验证通过才导入到数据库
        if (!StringUtils.isEmpty(errorMsg.toString())) {
            throw new AppBadRequestException(errorMsg.toString());
        }
        log.info("导入成功，共" + wordsList.size() + "条数据！");
        return wordsList;
    }

    /**
     * 获取课程单词
     *
     * @param page
     * @param bookId
     * @param form
     * @return
     */
    @Override
    public Page<Words> getAllWordsByCourse(Page<Words> page, Integer bookId, WordsSelectionForm form) {
        List<Words> words = baseMapper.selectAllWordsByCourse(page, bookId, form);
        page.setRecords(words);
        return page;
    }
}
