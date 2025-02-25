# **생기**
> 생일 하나는 기막히게 챙기는

누구에게나 일 년에 단 하루밖에 없는 생일은 소중한 날입니다.  
선물과 같은 부담에서 벗어나 편지로 마음을 전하며
축하하고 축하받는 모두가 행복하길 기대하는 분들을 위한 애플리케이션입니다.  
소중한 사람과의 추억을 생기와 함께 떠올리고 또 간직하시길 바랍니다.

---

# 주요 기능
**'생기'는 3개의 탭으로 이루어져 있습니다.**  
앱의 이미지는 생일 케이크로, 소중한 인연을 만나러 가는 길의 설렘과 추억을 담았습니다.

## 1. 연락처
- 첫 번째 탭은 연락처 탭입니다. 인연을 검색, 열람, 추가, 삭제할 수 있는 기능 등이 있습니다.
  - **검색**: SearchView를 사용하였으며 이름과 생일 모두로 검색이 가능합니다.
  - **열람**: ProfileAdapter와 RecyclerView로 구현한 각 프로필을 클릭하면 상세 페이지로 연결됩니다. ProfileData Class 내부에 SnsData class를 만들어서, 상세 페이지를 열람했을 때에만 연락처 탭에서 볼 수 없었던 전화번호와 sns프로필을 SnsAdapter를 이용하여 RecyclerView로 띄워줍니다. 그 속의 Sns 아이콘이나 번호를 클릭하면 Intent를 통해 SnsData 속 정보를 전달하여 전화, 문자, 인스타 프로필로 연결되어, 추후에 작성될 편지를 전달하며 생일을 축하해줄 수 있습니다.
  - ![search](https://github.com/Jannare/madcamp_week1/assets/173989725/fa6c1943-e20d-4e6a-a149-74ea8588e134)
  - **추가**: 우측 하단의 네잎클로버 버튼을 누르면 인연을 추가할 수 있습니다. ImageButton을 클릭하여 갤러리에서 불러온 프로필 사진을 추가하고, EditText에 이름, 전화번호, 생일, 인스타그램 ID를 입력하여 정보를 받아와 인연을 추가합니다.
  - **삭제**: 연락처의 RecyclerView 속 각 아이템을 LongClick하면 AlertDialog로 확인한 후 삭제할 수 있습니다.
  - ![add](https://github.com/Jannare/madcamp_week1/assets/173989725/35657ffd-9d41-4c88-a350-ff8dc49d77e1)
## 2. 갤러리
- 두 번째 탭은 갤러리 탭입니다. 갤러리 탭에서는 폴더를 열람하여 사진을 보는 기능과, 카메라를 통한 촬영으로 사진 추가하기, 갤러리에서 사진 불러오기 기능이 있습니다.
  - **폴더**: Recycler view를 활용하여 월별 추억을 담은 폴더를 정렬하였습니다.
  - **카메라 버튼**: MediaStore.Action_IMAGE_CAPTURE를 통해 카메라 촬영 기능을 삽입하였으며 저장기능을 구현하였습니다. 촬영한 사진은 Intent 함수를 통해 갤러리에 자동으로 저장되며 애플리케이션 내에도 저장됩니다.
  - **갤러리 버튼**: 사용자의 API 버전에 따라 권한을 요청하고 선택한 이미지 데이터를 string 형태로 전송하여 갤러리에 추가하는 기능을 구현하였습니다. 이미지의 촬영 정보가 포함되어 갤러리로 들어갈 시 사진 촬영 시간을 확인 가능합니다. sharedPreferences를 활용해 데이터를 보존하였습니다. 사진을 꾹 눌러서 삭제할 수도 있습니다.
  - ![gallery](https://github.com/Jannare/madcamp_week1/assets/173989725/a73b9ead-0b8b-4e27-8498-b6a17eb292ed)
  - 연락처와 편지 탭 사이에 갤러리 탭을 위치한 이유는, 편지를 쓰러 가기 전에 그 사람과의 추억을 둘러보고, 편지를 쓰는 데에 참고한다는 User Flow를 예상하였습니다.

## 3. 편지
- 세 번째 탭은 편지 탭입니다. 편지 탭에서는 다양한 계절감, 폰트, 텍스티콘을 이용해 전송 대상에 맞게 편지를 작성하고 이미지로 저장할 수 있습니다.
  - **계절**: 버튼을 클릭하면 Layout의 background가 다르게 설정됩니다. 봄, 여름, 가을, 겨울 테마가 있습니다. API Level이 낮아도 이미지가 잘 돌아갈 수 있도록 Bitmap을 생성하고 이미지의 픽셀을 조정하는 과정이 있었습니다.
  - **폰트**: EditTextView에 작성하던 글의 바꾸고 싶은 부분을 드래그 하고 버튼을 클릭하면 그 부분의 폰트가 다르게 설정됩니다. 기본, 귀염, 공손, 자유로운 느낌을 주는 네 가지 폰트가 있습니다. SpannableStringBuilder와, TypefaceSpan을 상속받는 CustomTypefaceSpan 객체를 활용하였습니다.
  - **텍스티콘**: 버튼을 클릭하면 현재까지 작성된 부분의 뒤에 텍스티콘이 추가됩니다. 8개의 감정들이 있으며, 기분에 따라 다양하게 사용하실 수 있습니다.
  - **저장**: 저장 버튼을 클릭하면 편지지와 편지 글이 이미지로 저장됩니다. 그냥 다운로드 하면 내부 저장소의 다운로드에 저장이 되므로, 갤러리 접근 권한을 요청하여서 갤러리에 저장될 수 있도록 구현하였습니다.
  - ![letter](https://github.com/Jannare/madcamp_week1/assets/173989725/e5303dd4-c494-4a0c-aa9e-e062b6a38110)

  이러한 기능을 가진 탭들은 완성된 편지지를 '연락처의 프로필 속 Sns를 통해 전송'하거나, '갤러리에 아카이빙'하는 등의 방식을 통해서 유기적으로 연결됩니다.

---

# 기타

- **개발 환경**: AndroidStudio (Kotlin), Adobe Illustrator, Github
- **요구사항**: Android SDK 버전 24 이상
- **제작자**: 김수환, 신서원
- **APK**: https://drive.google.com/drive/folders/1puQoVuei7koITHHls6YV8yCHJkNg2Qtu?usp=sharing
