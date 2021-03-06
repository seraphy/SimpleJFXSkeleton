package jp.seraphyware.javafxexam.jfxexam1.ui.inner;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import jp.seraphyware.javafxexam.jfxexam1.ui.common.AbstractDocumentController;

public class Page1Controller extends AbstractDocumentController implements Initializable {

	@FXML
	private TextArea textarea;

	@Override
	protected void makeRoot() {
		FXMLLoader ldr = new FXMLLoader();
		URL url = getClass().getResource("/ui/inner/Page1.fxml"); //$NON-NLS-1$
		assert url != null;

		ldr.setLocation(url);
		ldr.setController(this);

		try {
			setRoot(ldr.load());

		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
}
