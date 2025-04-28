// Flashメッセージの表示
$(function() {
	$(".alert").fadeOut(5000);
});

// 削除の確認
function checkDelete() {
	var result = confirm('削除したデータは元にもどせません。\r\n削除してよろしいですか？');
	if (result == true) {
		return true;
	} else {
		return false;
	}
}

// 更新の確認
function checkUpdate() {
	var result = confirm('データが上書きされます。\r\n更新してよろしいですか？');
	if (result == true) {
		return true;
	} else {
		return false;
	}
}

// クラス一括登録の確認
function checkCreateClass() {
	var result = confirm('一括登録後、自動で在籍生徒に名前の順で出席番号が割り当てられます。\r\n一括登録してよろしいですか？');
	if (result == true) {
		return true;
	} else {
		return false;
	}
}

// クラス個別登録の確認
function checkAddClass() {
	var result = confirm('追加された生徒には、一番最後の出席番号が割り当てられます。\r\n追加してよろしいですか？');
	if (result == true) {
		return true;
	} else {
		return false;
	}
}

// クラス削除の確認
function checkDeleteClass() {
	var result = confirm('削除後、自動で在籍生徒に名前の順で出席番号が割り当てられます。\r\n削除してよろしいですか？');
	if (result == true) {
		return true;
	} else {
		return false;
	}
}

// アップロード画像のファイルサイズを制限
const fileInput = document.getElementById('fileInput');
fileInput.addEventListener('change', function(event) {
	const file = event.target.files[0];
	const maxSize = 1 * 1024 * 1024; // 1MB
	if (file.size > maxSize) {
		alert('アップロードできる写真のサイズは最大1MBまでです。');
		fileInput.value = '';
	}
});

