package com.aem.community.core.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.commerce.common.ValueMapDecorator;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.EmptyDataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;

//import com.google.gson.Gson;

@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Promo forum component Dropdown option fetch Servlet",
		"sling.servlet.resourceTypes=/apps/blog/dailog/dropdown", "sling.servlet.methods=" + HttpConstants.METHOD_GET })
public class DemoDailogDropdown extends SlingSafeMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		// logger.info("Before try");
		try {

			// set fallback
			request.setAttribute(DataSource.class.getName(), EmptyDataSource.instance());
			ResourceResolver resolver = request.getResourceResolver();
			// Create an ArrayList to hold data
			List<Resource> fakeResourceList = new ArrayList<Resource>();
			ValueMap vm = null;

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

			// Add 5 values to drop down!
			for (int i = 0; i < colourArr.length; i++) {
				// allocate memory to the Map instance
				vm = new ValueMapDecorator(new HashMap<String, Object>());
				// Specify the value and text values
				String Value = colourArr[i].trim();
				String Text = colourArr[i].trim();

				// populate the map
				vm.put("value", Value);
				vm.put("text", Text);
				if(i==100) {
					vm.put("selected", true);
				}
				fakeResourceList.add(new ValueMapResource(resolver, new ResourceMetadata(), "nt:unstructured", vm));
			}

			// Create a DataSource that is used to populate the drop-down control
			DataSource ds = new SimpleDataSource(fakeResourceList.iterator());
			request.setAttribute(DataSource.class.getName(), ds);
		} catch (Exception e) {
			logger.info("Error in Get Drop Down Values", e);
		}
	}

}
