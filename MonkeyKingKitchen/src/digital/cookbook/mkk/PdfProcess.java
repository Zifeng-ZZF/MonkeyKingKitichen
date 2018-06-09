package digital.cookbook.mkk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.GroupLayout.Alignment;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.sun.javadoc.Doc;

public class PdfProcess {
	/**
	 * Export recipe as pdf
	 * @param recipe
	 */
	public void exportPDF(Recipe recipe,String path) {
		
		FileOutputStream fOutputStream;
		
		try {
			fOutputStream = new FileOutputStream(new File(path));
			PdfWriter pdfWriter = new PdfWriter(fOutputStream);
			PdfDocument pdfDocument = new PdfDocument(pdfWriter);
			Paragraph partition = new Paragraph("\r\n\r\n");
			
			Document document = new Document(pdfDocument, PageSize.A4);
			
			//Title
			Text title = new Text(recipe.getName());
			PdfFont titleFont = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
			title.setFont(titleFont);
			title.setFontSize(40);
			title.setFontColor(new DeviceRgb(255,255,255));
			title.setBackgroundColor(new DeviceRgb(170, 34, 68),40,40,30,30);
			Paragraph titlePara = new Paragraph(title);
			titlePara.setFixedPosition(40, PageSize.A4.getHeight()-80, 500);
			document.add(titlePara);
			System.out.println("ss");
			
			document.add(partition);
			document.add(partition);
			
			//Cooking time && preparation time && servings 
			Text times = new Text("Cooking time: " + recipe.getCookingTime() + "minutes     "
					+ "Preparation time: " + recipe.getPreparationTime() + "minutes      "
					+ "Servings: " + recipe.getServings());
			PdfFont timeFont = PdfFontFactory.createFont(FontConstants.HELVETICA);
			times.setFont(timeFont);
			times.setFontSize(14);
			times.setFontColor(new DeviceRgb(68, 34, 34));
			document.add(new Paragraph(times));
			
			document.add(new Paragraph("\r\n"));
			
			//Ingredients.
			Text ingredientTitle = new Text("INGREDIENT");
			ingredientTitle.setFontColor(Color.WHITE);
			ingredientTitle.setBackgroundColor(new DeviceRgb(68, 187, 34),5,5,2,2);
			ingredientTitle.setFontSize(17);
			document.add(new Paragraph(ingredientTitle));
			
			String ingredients = "";
			for (Ingredient ingredient : recipe.getIngredients()) {
				String oneIngredient = ingredient.getName()+"*"+ingredient.getAmount()+" "+ingredient.getUnit();
				if(!ingredient.getProcessMethod().equals(""))
					oneIngredient += (" (" + ingredient.getProcessMethod() + "),  ");
				else
					oneIngredient += ",  ";
				ingredients += oneIngredient;
			}
			Paragraph ingredientPara = new Paragraph(ingredients);
			ingredientPara.setFontSize(12);
			document.add(ingredientPara);
			
			document.add(partition);
			
			
			//Steps
			Text stepTitle = new Text("STEPS:");
			stepTitle.setFontColor(Color.WHITE);
			stepTitle.setBackgroundColor(new DeviceRgb(255, 136, 51),5,5,2,2);
			stepTitle.setFontSize(17);
			document.add(new Paragraph(stepTitle));
			
			List stepList = new List();
			for (String step: recipe.getPreparationSetps()) {
				stepList.add(new ListItem(step));
			}
			stepList.setFontSize(15);
			document.add(stepList);
			
			document.add(partition);
			
			//Water Mark
			Text waterMark = new Text("Monkey King Kitchen");
			waterMark.setFont(PdfFontFactory.createFont(FontConstants.TIMES_BOLD));
			waterMark.setFontColor(new DeviceRgb(255, 136, 51));
			waterMark.setFontSize(25);
			
			document.add(new Paragraph(waterMark).setFixedPosition(PageSize.A4.getWidth()-300, 20, 500));
			
			document.close();
			fOutputStream.flush();
			fOutputStream.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
