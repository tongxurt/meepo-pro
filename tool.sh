
# 生成sign
keytool -genkey -alias android.keystore -keyalg RSA -validity 20000 -keystore android.keystore
# 旧版升级新版
keytool -importkeystore -srckeystore android.keystore -destkeystore android.keystore -deststoretype pkcs12
# 修改别名
keytool -changealias -keystore  key.keystore -alias android.keystore -destalias key1
# 查看信息
keytool -list -v -keystore key.keystore