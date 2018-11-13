package jp.seraphyware.javafxexam.jfxexam1;

import java.awt.SplashScreen;

import javax.swing.SwingUtilities;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import jp.seraphyware.javafxexam.jfxexam1.ui.MainFrameController;

/**
 * アプリケーションエントリ
 * @param args
 */
public class MainApp extends Application {

	/**
	 * アプリケーションの開始
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			// 明示的に終了させる.
			Platform.setImplicitExit(false);

			// メインフレームを構築する
			MainFrameController mainFrameCtrl = new MainFrameController();
			mainFrameCtrl.setDestroyCallback(() -> {
				// 閉じるボタンまたはCloseコマンドにより、メインフレームを閉じてアプリケーションを終了する
				mainFrameCtrl.closeWindow();
				Platform.exit();
			});
			mainFrameCtrl.openWindow();

			// スプラッシュスクリーンを閉じる
			closeSplashScreen();

		} catch (Exception ex) {
			ex.printStackTrace(); // ログの設定に失敗している可能性があるためコンソールへ
			throw ex;
		}
	}

	/**
	 * スプラッシュスクリーンがあれば閉じる.
	 */
	private void closeSplashScreen() {
		// スプラッシュスクリーンの取得(表示されていれば)
		SwingUtilities.invokeLater(() -> {
			try {
				SplashScreen splashScreen = SplashScreen.getSplashScreen();
				if (splashScreen != null) {
					// スプラッシュを閉じる
					splashScreen.close();
				}
			} catch (Exception ex) {
				// スプラッシュ取得エラーは無視して良い.
				ex.printStackTrace();
			}
		});
	}

	/**
	 * アプリケーションの停止
	 */
	@Override
	public void stop() throws Exception {
		System.out.println("done.");
		System.exit(0);
	}

	/**
	 * メインエントリ
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
