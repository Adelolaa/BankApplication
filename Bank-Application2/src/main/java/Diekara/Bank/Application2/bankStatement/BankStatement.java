package Diekara.Bank.Application2.bankStatement;

import com.itextpdf.text.*;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

@Slf4j
public class BankStatement {


    private static final String FILE ="C:\\Users\\serah.aleshinloye\\Documents\\BankStatement.pdf";

    public static void generateStatement() throws FileNotFoundException, DocumentException {
        Rectangle rectangle =new Rectangle(PageSize.A4);

        Document document = new Document();
        log.info("setting size of document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();
        document.add(new Paragraph("Sample text"));
        document.add(new Chunk("TJA Bank Statement"));
        document.add(new Phrase("I'm enjoying this"));

        document.add(new Paragraph(" "));

        Anchor anchor =new Anchor("IACADEMY");
        anchor.setReference("https://africaprudential.com/i-academy//");

        List orderedList = new List(List.ORDERED);
        orderedList.add(new ListItem("Emperor"));
        orderedList.add(new ListItem("Noah"));

        List unOrderedList = new List(List.UNORDERED);
        unOrderedList.add(new ListItem("Testing"));
        unOrderedList.add(new ListItem("Testing2"));

        log.info("populating list");
        String[]firstNamesArray = {"Adeolu", "Oyin", "Noah"};
        String[] lastNamesArray = {"Oduniyi", "Alasoluyi", "Johnson"};
        log.info("setting table to 3 columns");
        PdfPTable table = new PdfPTable(3);
        PdfPCell serialNo = new PdfPCell(new Paragraph("S/N"));
        PdfPCell names = new PdfPCell(new Paragraph("Firstname"));
        PdfPCell lastname = new PdfPCell(new Paragraph("Lastname"));

       table.addCell(serialNo);
       table.addCell(names);
       table.addCell(lastname);

       for (int i=0; i<firstNamesArray.length; i++){
           PdfPCell serialNo1 = new PdfPCell(new Paragraph((String.valueOf(i+1))));
           PdfPCell firstName = new PdfPCell(new Paragraph(firstNamesArray[i]));
           PdfPCell lastName = new PdfPCell(new Paragraph(lastNamesArray[i]));

           table.addCell(serialNo1).setBackgroundColor(BaseColor.PINK);
           table.addCell(firstName);
           table.addCell(lastname);

       }


       document.add(table);
       document.add(orderedList);
       document.add(unOrderedList);

        document.add(anchor);

        document.close();

        log.info("File has been created");


    }

    public static void main(String[] args) throws DocumentException, FileNotFoundException {
        generateStatement();

    }
}
