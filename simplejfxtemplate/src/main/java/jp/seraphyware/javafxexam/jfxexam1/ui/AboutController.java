package jp.seraphyware.javafxexam.jfxexam1.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jp.seraphyware.javafxexam.jfxexam1.ui.common.AbstractWindowController;

public class AboutController extends AbstractWindowController implements Initializable {

	@FXML
	private WebView webview;

	{
		setSizeToScene(false); // ウィンドウサイズの自動フィットをしない
	}

	public void showAndWait() {
		Stage stg = getStage();
		stg.initModality(Modality.WINDOW_MODAL);
		stg.showAndWait();
	}

	@Override
	protected Stage createStage() {
		Stage stg = super.createStage();
		stg.setTitle(resources.getString("about.title"));
		return stg;
	}

	@Override
	public void onCloseRequest(WindowEvent event) {
		onClose();
	}

	@Override
	protected void makeRoot() {
		FXMLLoader ldr = new FXMLLoader();
		URL url = getClass().getResource("/ui/About.fxml"); //$NON-NLS-1$
		assert url != null;

		ldr.setResources(resources);
		ldr.setController(this);
		ldr.setLocation(url);

		try {
			setRoot(ldr.load());

		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		WebEngine engine = webview.getEngine();

		// HTMLリソースのロード
		String docPath = resources.getString("about.resourceName"); //$NON-NLS-1$
		ClassLoader ldr = AboutController.class.getClassLoader();
		try (InputStream is = ldr.getResourceAsStream(docPath)) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			if (is != null) {
				byte[] buf = new byte[4096];
				for (;;) {
					int rd = is.read(buf);
					if (rd < 0) {
						break;
					}
					bos.write(buf, 0, rd);
				}
			}
			engine.loadContent(new String(bos.toByteArray(), StandardCharsets.UTF_8));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	@FXML
	protected void onClose() {
		closeWindow();
	}

	@FXML
	protected void onShowSysProps() {
		StringBuilder buf = new StringBuilder();

		// System Properties
		buf.append("[System Properties]\r\n");
		Properties props = System.getProperties();
		buf.append(Collections.list(props.propertyNames()).stream().sorted()
				.map(key -> key + "=" + System.getProperty(key.toString()))
				.collect(Collectors.joining("\r\n", "", "\r\n")));

		// Environments
		buf.append("\r\n[Environments]\r\n");
		Map<String, String> env = System.getenv();
		buf.append(env.entrySet().stream()
				.sorted((a, b) -> String.CASE_INSENSITIVE_ORDER
						.compare(a.getKey(), b.getKey()))
				.map(entry -> entry.getKey() + "=" + entry.getValue())
				.collect(Collectors.joining("\r\n", "", "\r\n")));

		TextArea node = new TextArea();
		node.setText(buf.toString());
		node.setEditable(false);

		Alert alert = new Alert(AlertType.NONE);
		alert.initOwner(getStage());
		alert.getButtonTypes().setAll(ButtonType.OK);
		alert.setTitle(resources.getString("sysprops.title")); //$NON-NLS-1$
		alert.getDialogPane().setContent(node);
		alert.setResizable(true);
		alert.setWidth(450);
		alert.setHeight(450);
		alert.showAndWait();
	}
}
