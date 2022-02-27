package interview;

import java.io.File;
import java.io.FileInputStream;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import org.yaml.snakeyaml.Yaml;

public class ReadData {

	public static final String userDir = System.getProperty("user.dir");

	public static final String testDataDir = "\\src\\data\\";

	private static final String DATAFILELOCATION = userDir + testDataDir;

	// read data from YAML file
	@DataProvider(name = "dataMap")
	public static Object[][] dataReader(ITestContext context) throws Exception {
		String dataFile = context.getCurrentXmlTest().getParameter("dataFile");
		String testCaseName = context.getCurrentXmlTest().getName().toString();

		InputStream inputStream = new FileInputStream(new File(DATAFILELOCATION + dataFile));
		Yaml yaml = new Yaml();

		Map<String, Object> data = yaml.load(inputStream);

		JSONObject jsonObject = new JSONObject(data);
		JSONObject testData = jsonObject.getJSONObject(testCaseName);

		Object[][] myDataMap = new Hashtable[1][1];
		// Read data from row that have Test Name value equal to testCaseName
		Map<Object, Object> datamap = new Hashtable<Object, Object>();

		Iterator<String> keys = testData.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			try {
				datamap.put(key.toString(), testData.get(key).toString());
			} catch (Exception e) {
				datamap.put(key.toString(), "");
			}
		}
		myDataMap[0][0] = datamap;
		return myDataMap;
	}
}
