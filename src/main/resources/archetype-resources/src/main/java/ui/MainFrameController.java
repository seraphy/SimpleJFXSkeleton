#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.ui;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.LinkedList;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ${package}.ui.common.AbstractWindowController;
import ${package}.ui.inner.Page1Controller;
import ${package}.ui.inner.Page2Controller;
import ${package}.ui.util.ErrorDialogUtils;


/**
 * メイン画面
 */
public class MainFrameController extends AbstractWindowController {

	private Runnable destroyCallback;

	public Runnable getDestroyCallback() {
		return destroyCallback;
	}

	public void setDestroyCallback(Runnable destroyCallback) {
		this.destroyCallback = destroyCallback;
	}

	@FXML
	private BorderPane root;

	@FXML
	private MenuBar menuBar;

	@Override
	protected void makeRoot() {
		BorderPane root;
		FXMLLoader ldr = new FXMLLoader();
		ldr.setResources(resources);
		ldr.setController(this);
		ldr.setLocation(getClass().getResource("/ui/MainFrame.fxml")); //${symbol_dollar}NON-NLS-1${symbol_dollar}

		try {
			root = ldr.load();
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}

		setRoot(root);
	}


	@Override
	public void openWindow() {
		// タイトルの設定
		String title = resources.getString("application.title"); //${symbol_dollar}NON-NLS-1${symbol_dollar}
		Stage stg = getStage();
		stg.setTitle(title);
		super.openWindow();
	}

	/**
	 * 閉じて良いか確認してからウィンドウを閉じる.
	 * @return 閉じた場合はtrue、キャンセルした場合はfalse
	 */
	public boolean performClose() {
		destroy();

		getStage().toFront(); // 対象のドキュメントを前面にだしてから問い合わせる

		// ドキュメントを閉じて良いか確認する.
		Alert closeConfirmAlert = new Alert(AlertType.CONFIRMATION);
		closeConfirmAlert.initOwner(getStage());
		closeConfirmAlert.setHeaderText(resources.getString("mainFrame.closeConfirm")); //${symbol_dollar}NON-NLS-1${symbol_dollar}
		Optional<ButtonType> result = closeConfirmAlert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			if (destroyCallback != null) {
				destroyCallback.run();
				return true;
			}
		}
		return false;
	}

	@Override
	public void onCloseRequest(WindowEvent event) {
		event.consume();
		performClose();
	}

	/**
	 * 子ウィンドウを破棄するためのdisposerのリスト<br>
	 */
	private final LinkedList<Runnable> disposers = new LinkedList<>();

	protected void destroy() {
		disposeCenterPane();
	}

	protected void disposeCenterPane() {
		root.setCenter(null);

		disposers.forEach(Runnable::run);
		disposers.clear();
	}

	@FXML
	protected void onFileClose() {
		try {
			performClose();

		} catch (Exception ex) {
			ex.printStackTrace();
			Stage stage = getStage();
			ErrorDialogUtils.showException(stage, ex);
		}
	}

	@FXML
	protected void onAbout() {
		AboutController ctrl = new AboutController();
		ctrl.setOwner(getStage());
		ctrl.showAndWait();
	}

	@FXML
	protected void onPage1() {
		try {
			disposeCenterPane();
			Page1Controller ctrl = new Page1Controller();
			disposers.add(() -> {
				// do nothing.
			});

			Parent contentRoot = ctrl.getRoot();
			root.setCenter(contentRoot);

		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialogUtils.showException(getStage(), ex);
		}
	}

	@FXML
	protected void onPage2() {
		try {
			disposeCenterPane();
			Page2Controller ctrl = new Page2Controller();
			disposers.add(() -> {
				// do nothing
			});

			Parent contentRoot = ctrl.getRoot();
			root.setCenter(contentRoot);

		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialogUtils.showException(getStage(), ex);
		}
	}
}
