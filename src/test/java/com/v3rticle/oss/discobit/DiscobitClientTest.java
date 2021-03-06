package com.v3rticle.oss.discobit;

import java.io.File;
import java.net.URL;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.v3rticle.oss.discobit.client.DiscobitClient;
import com.v3rticle.oss.discobit.client.DiscobitClientFactory;
import com.v3rticle.oss.discobit.client.DiscobitOperationException;
import com.v3rticle.oss.discobit.client.bootstrap.DiscobitOptions;

/**
 * @author jens@v3rticle.com
 *
 */
public class DiscobitClientTest {

	static DiscobitClient discobit;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.setProperty(DiscobitOptions.DISCOBIT_SERVER_URL, "http://127.0.0.1:9999");
		System.setProperty(DiscobitOptions.DISCOBIT_SERVER_USERNAME, "adminadmin");
		System.setProperty(DiscobitOptions.DISCOBIT_SERVER_PASSWORD, "adminadmin");
		System.clearProperty(DiscobitOptions.DISCOBIT_CONFIG_UUID);
		discobit = DiscobitClientFactory.getClient();
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void createAndReadConfiguration() {
		
		try {
			
			int spaceID = discobit.createApplicationSpace("junit", "junit@v3rticle.com", "junit-app-"+ System.currentTimeMillis(), "http://v3rticle.com");
			Assert.assertTrue(spaceID >= 0);
					
			UUID cUUID = discobit.createConfiguration(spaceID, "junit-cfg-" + System.currentTimeMillis(), "test configuration");
			Assert.assertNotNull(cUUID);
			
			boolean success = discobit.createConfigProperty(cUUID, "test1", "value1");
			Assert.assertTrue(success);
			
			String value = discobit.getConfigProperty(cUUID,"test1", false);
			Assert.assertEquals("value1", value);
			
			URL url = this.getClass().getClassLoader().getResource("test.properties");
			File f = new File(url.getFile());
			Assert.assertTrue(f.exists());

			boolean successPush = discobit.pushConfiguration(cUUID.toString(), f);
			Assert.assertTrue(successPush);
			
			
		} catch (DiscobitOperationException e) {
			Assert.fail(e.getMessage());
		}
		
	}

}
