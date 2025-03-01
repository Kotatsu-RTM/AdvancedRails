> **Note**  
> 本リポジトリは[`AdvancedRails`](https://github.com/Hongmen-Technology-Industries/AdvancedRails)のForkであり、  
> 描画スクリプトをJVM言語へと書き換えるプロジェクトの一環として開発されています。  
> 上流のプロジェクトとの同期が考慮されていないため、問題が発生する場合があります。

---

# Advanced Rails

[![Build](https://github.com/Kotatsu-RTM/AdvancedRails/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/Kotatsu-RTM/AdvancedRails/actions/workflows/build.yml)  
[![MCVer](https://img.shields.io/badge/Minecraft-1.12.2-brightgreen)](https://www.minecraft.net/)
[![ForgeVer](https://img.shields.io/badge/Forge-14.23.5.2860-important)](https://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.12.2.html)
[![RTMVer](https://img.shields.io/badge/RealTrainMod-2.4.23--42-informational)](https://www.curseforge.com/minecraft/mc-mods/realtrainmod/files/3873403)

## 動作環境

必須

- RealTrainMod 1.12.2 ([NGT](https://www.curseforge.com/minecraft/mc-mods/realtrainmod))
- RTM Wire51 Pack ([Link](https://www.twitter.com/hi03_s/status/914244174433497088))

推奨

- RAM 16GB以上
- fixRTM ([Link](https://www.curseforge.com/minecraft/mc-mods/fixrtm))

## 導入

1. 既に導入されている場合、`Kozu_RE_X.zip`・`Better_Rail_Electronics.zip`・`Advanced_Rails.zip`を`mods`フォルダから削除
2. `AdvancedRails-*.jar`を`mods`フォルダにコピー
3. Minecraftを起動し、各モデルが追加されていることを確認

## 中身

当パックのモデルはタグ `AR`・`BRE` で検索可能です。

### レールモデル

- PC枕木
  - バラスト:有/無/ローハイト
  - 状態:新/古
  - 軌間:1067mm/1435mm
  - 壁:左/右
- スラブ軌道
  - 軌間:1067mm/1435mm
  - 壁:左/右
- 盛土
  - 位置:左/右
- 橋枕木(無道床)
  - 1067mm/1435mm
- 脱線防止ガード
  - 位置:左/右
- 擁壁
  -位置:左/右
- 円度杭（分離）（左右共通）
- 新宿駅ホーム屋根
  - 中央快速線ホーム
  - 中央線特急ホーム
- 島式用ホーム屋根
  - 幅:7m/9m
  - 色:青/緑/水色/オレンジ
  - ラインカラー:有/無
  - 柱:有/無
- 対向式用ホーム屋根
  - 幅:3m/7m
  - 位置:左/右
- 壁
  - スタイル:東武風壁/ブロック塀/ネットフェンス
  - 位置:左/右
- ホーム
  - 床面
  - 高機能ホーム端
    - 位置:左/右
- 笠石&誘導ブロック
  - 状態:新/古
  - 位置:左/右
- 複線橋桁（小田急多摩川橋梁スタイル）
- 複線トンネル（小田急代々木上原～梅が丘スタイル）

### ワイヤーモデル

- 角トラスビーム
- Vトラスビーム
- かご型トラスビーム
- H鋼ビーム
  - 色:緑/灰
- 鋼管ビーム
  - 方杖:有/無
- 単ビーム
- シンプルカテナリー 40m
- 直流き電線 40m

### 碍子モデル

- き電線やぐら・碍子
  - 方角:東/西/南/北
- H鋼ビーム用接続ポイント
- コンクリート架線柱
  - 架線高:通常/W51
  - オフセット:有/無
- トラス架線柱
  - 架線高:通常/W51
  - オフセット:有/無
- 鋼管架線柱
  - 架線高:通常/W51
  - オフセット:有/無

### 照明モデル

- ビーム吊下信号機用支柱
- 障害物検知装置 標準 10:1ブロックL・R
  - 方向:正面/10:1ブロックL・R
- 乗降台
  - 手すり:有/無
- 信号柱
  - 素材:鋼管/コンクリート
  - 高さ:7m/6m/5m
- 機器収納箱
- ホッチキス
  - オフセット:有/無
- 待合室
- 絶縁はしご
  - 素材:竹/FRP
  - 設置方式:地面設置/壁設置

### 列車検知器モデル

- 踏切バックアップ装置(地上子)

### ATCモデル

- ATACS位置補正地上子
  - 種類:ロング/ショート

## その他

> **Note**  
> このリポジトリでは描画スクリプトの修正のみを行っておりますので、  
> モデルの追加要望等は上流のリポジトリにお願いします。

~~モデルの追加要望や問題等はお気軽にリポジトリのissueやメンバーのDMにどうぞ~~  
~~また、ご自身のモデルをこのパックに追加したい場合はホンメンのメンバーでなくてもフォークをしてプルリクエストを送信して頂ければ追加いたします~~

## 更新履歴

|     日付     | バージョン | 内容            |
|:----------:|:-----:|:--------------|
| 2021/09/17 | 1.0.0 | 公開            |
| 2021/09/18 | 1.1.0 | モデル追加、CC-BY適用 |
| 2021/09/19 | 1.1.2 | 架線柱に一部差分を追加   |
| 2021/09/21 | 1.2.0 | モデル追加         |
| 2021/09/29 | 1.3.0 | モデル追加 名称変更    |
| 2021/10/14 | 1.4.0 | モデル追加 権利を移管   |
| 2021/10/24 | 1.5.0 | モデル追加         |