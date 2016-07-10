# login, sigin, user name valid
$Host:port/KOPM/userLogin?userName=xxxxx&passWord=xxxxx   
$Host:port/KOPM/userSignin?userName=xxxxx&passWord=xxxxx
$Host:port/KOPM/judgeUsernameValid?userName=xxxxx

# rank list record
# return json object, score in decreasing order
# example as follows:
{
	"totalProperty": 2,
	"root": [
		{
			"uid": 1,
			"username": "davioli",
			"password": "123456",
			"score": 20
		},
		{
			"uid": 2,
			"username": "dirk",
			"password": "654321",
			"score": 20
		}
	]
}
$Host:port/KOPM/ajaxGetRankList?userName=xxxx
