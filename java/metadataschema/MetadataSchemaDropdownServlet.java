package com.acc.aem64.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acc.aem64.core.servlets.pojo.MetadataSchemaDropdownPojo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Metadata Schema Dropdown json Servlet",
		"sling.servlet.paths=/bin/metadata/dropdown1", "sling.servlet.methods=" + HttpConstants.METHOD_GET })
public class MetadataSchemaDropdownServlet extends SlingSafeMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Type", "application/json");
			Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

			String[] colourArr = { "AliceBlue ", "AntiqueWhite ", "Aqua ", "Aquamarine ", "Azure ", "Beige ", "Bisque ",
					"Black ", "BlanchedAlmond ", "Blue ", "BlueViolet ", "Brown ", "BurlyWood ", "CadetBlue ",
					"Chartreuse ", "Chocolate ", "Coral ", "CornflowerBlue ", "Cornsilk ", "Crimson ", "Cyan ",
					"DarkBlue ", "DarkCyan ", "DarkGoldenRod ", "DarkGray ", "DarkGrey ", "DarkGreen ", "DarkKhaki ",
					"DarkMagenta ", "DarkOliveGreen ", "DarkOrange ", "DarkOrchid ", "DarkRed ", "DarkSalmon ",
					"DarkSeaGreen ", "DarkSlateBlue ", "DarkSlateGray ", "DarkSlateGrey ", "DarkTurquoise ",
					"DarkViolet ", "DeepPink ", "DeepSkyBlue ", "DimGray ", "DimGrey ", "DodgerBlue ", "FireBrick ",
					"FloralWhite ", "ForestGreen ", "Fuchsia ", "Gainsboro ", "GhostWhite ", "Gold ", "GoldenRod ",
					"Gray ", "Grey ", "Green ", "GreenYellow ", "HoneyDew ", "HotPink ", "IndianRed  ", "Indigo   ",
					"Ivory ", "Khaki ", "Lavender ", "LavenderBlush ", "LawnGreen ", "LemonChiffon ", "LightBlue ",
					"LightCoral ", "LightCyan ", "LightGoldenRodYellow ", "LightGray ", "LightGrey ", "LightGreen ",
					"LightPink ", "LightSalmon ", "LightSeaGreen ", "LightSkyBlue ", "LightSlateGray ",
					"LightSlateGrey ", "LightSteelBlue ", "LightYellow ", "Lime ", "LimeGreen ", "Linen ", "Magenta ",
					"Maroon ", "MediumAquaMarine ", "MediumBlue ", "MediumOrchid ", "MediumPurple ", "MediumSeaGreen ",
					"MediumSlateBlue ", "MediumSpringGreen ", "MediumTurquoise ", "MediumVioletRed ", "MidnightBlue ",
					"MintCream ", "MistyRose ", "Moccasin ", "NavajoWhite ", "Navy ", "OldLace ", "Olive ",
					"OliveDrab ", "Orange ", "OrangeRed ", "Orchid ", "PaleGoldenRod ", "PaleGreen ", "PaleTurquoise ",
					"PaleVioletRed ", "PapayaWhip ", "PeachPuff ", "Peru ", "Pink ", "Plum ", "PowderBlue ", "Purple ",
					"RebeccaPurple ", "Red ", "RosyBrown ", "RoyalBlue ", "SaddleBrown ", "Salmon ", "SandyBrown ",
					"SeaGreen ", "SeaShell ", "Sienna ", "Silver ", "SkyBlue ", "SlateBlue ", "SlateGray ",
					"SlateGrey ", "Snow ", "SpringGreen ", "SteelBlue ", "Tan ", "Teal ", "Thistle ", "Tomato ",
					"Turquoise ", "Violet ", "Wheat ", "White ", "WhiteSmoke ", "Yellow ", "YellowGreen " };

			MetadataSchemaDropdownPojo msObj = new MetadataSchemaDropdownPojo();

			for (int i = 0; i < colourArr.length; i++) {
				String value = colourArr[i].trim();
				String text = colourArr[i].trim();
				MetadataSchemaDropdownPojo.Option option = new MetadataSchemaDropdownPojo.Option(text, value);
				msObj.addOption(option);
			}

			String json = gson.toJson(msObj);
			response.getWriter().write(json);

		}

		catch (Exception e) {
			logger.info("Error in Get Drop Down Values", e);
		}
	}

}