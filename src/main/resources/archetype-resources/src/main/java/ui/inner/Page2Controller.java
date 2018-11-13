#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.ui.inner;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ${package}.ui.common.AbstractDocumentController;

public class Page2Controller extends AbstractDocumentController implements Initializable {

	@FXML
	private VBox root;

	@FXML
	private Label labelNow;

	private static final long INTERVAL = 100 * 1000 * 1000L;

	private AnimationTimer timer;

	@Override
	protected void makeRoot() {
		FXMLLoader ldr = new FXMLLoader();
		URL url = getClass().getResource("/ui/inner/Page2.fxml"); //${symbol_dollar}NON-NLS-1${symbol_dollar}
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
		timer = new AnimationTimer() {
			private long last;

			@Override
			public void handle(long now) {
				if ((now - last) > INTERVAL) {
					last = now;
					onTimer();
				}
			}
		};
		onTimer();

		root.sceneProperty().addListener((self, old, scene) -> {
			if (scene != null) {
				timer.start();
			} else {
				timer.stop();
			}
		});
	}

	protected void onTimer() {
		labelNow.setText(new Date().toString());
	}
}
