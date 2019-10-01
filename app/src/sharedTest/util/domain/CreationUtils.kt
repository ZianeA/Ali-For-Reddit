package util.domain

import com.visualeap.aliforreddit.data.repository.redditor.RedditorEntity
import com.visualeap.aliforreddit.data.repository.post.PostEntity
import com.visualeap.aliforreddit.data.repository.post.PostWithRedditor
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditEntity
import com.visualeap.aliforreddit.domain.model.Account
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.model.Redditor
import com.visualeap.aliforreddit.domain.model.Subreddit
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import okhttp3.Credentials
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import kotlin.random.Random

const val ACCESS_TOKEN = "ACCESS TOKEN"
const val TOKEN_TYPE = "bearer"
const val REFRESH_TOKEN = "REFRESH TOKEN"
const val DEVICE_ID = "DEVICE ID"
const val REDIRECT_URL = "https://example.com/path"
const val CODE = "CODE"
const val ID = 101
const val NOT_SET_ROW_ID = 0
const val SINGLE_RECORD_ID = 1

fun createUserToken(
    id: Int = ID,
    accessToken: String = ACCESS_TOKEN,
    type: String = TOKEN_TYPE,
    refreshToken: String = REFRESH_TOKEN
) = UserToken(id, accessToken, type, refreshToken)

fun createUserlessToken(
    id: Int = ID,
    accessToken: String = ACCESS_TOKEN,
    type: String = TOKEN_TYPE,
    deviceId: String = DEVICE_ID
) = UserlessToken(id, accessToken, type, deviceId)

fun createToken(
    id: Int = ID,
    accessToken: String = ACCESS_TOKEN,
    type: String = TOKEN_TYPE
): Token {
    return if (Random.nextBoolean()) createUserlessToken(id, accessToken, type)
    else createUserToken(id, accessToken, type)
}

fun createAccount(
    id: Int = ID,
    redditor: Redditor = createRedditor(),
    token: UserToken = createUserToken()
) = Account(id, redditor, token)

//region Redditor
private const val REDDITOR_USERNAME = "FakeUsername"

fun createRedditor(username: String = REDDITOR_USERNAME) = Redditor(username)

fun createRedditorEntity(username: String = REDDITOR_USERNAME) =
    RedditorEntity(username)
//endregion

//region Subreddit
private const val SUBREDDIT_NAME = "FakeSubreddit"
private const val SUBREDDIT_ID = "FakeSubredditId"

fun createSubreddit(id: String = SUBREDDIT_ID, name: String = SUBREDDIT_NAME) =
    Subreddit(id, name)

fun createSubredditEntity(id: String = SUBREDDIT_ID, name: String = SUBREDDIT_NAME) =
    SubredditEntity(id, name)
//endregion

//region Post
private const val POST_ID = "FakePostId"
private const val POST_TITLE = "This is a fake title"
private const val POST_TEXT = "This is a fake text."
private const val POST_SCORE = 201
private const val POST_COMMENT_COUNT = 202
private const val POST_CREATED: Long = 1569878021

fun createPost(
    id: String = POST_ID,
    author: Redditor = createRedditor(),
    title: String = POST_TITLE,
    text: String = POST_TEXT,
    score: Int = POST_SCORE,
    commentCount: Int = POST_COMMENT_COUNT,
    subreddit: Subreddit = createSubreddit(),
    created: Long = POST_CREATED
) = Post(id, author, title, text, score, commentCount, subreddit, created)

fun createPostEntity(
    id: String = POST_ID,
    authorName: String = REDDITOR_USERNAME,
    title: String = POST_TITLE,
    text: String = POST_TEXT,
    score: Int = POST_SCORE,
    commentCount: Int = POST_COMMENT_COUNT,
    subredditId: String = SUBREDDIT_ID,
    created: Long = POST_CREATED
) = PostEntity(id, authorName, title, text, score, commentCount, subredditId, created)

fun createPostWithRedditor(
    postEntity: PostEntity = createPostEntity(),
    redditorEntity: RedditorEntity = createRedditorEntity(),
    subredditEntity: SubredditEntity = createSubredditEntity()
) = PostWithRedditor(postEntity, redditorEntity, subredditEntity)
//endregion

fun createBasicAuth(clientId: String = "CLIENT ID"): String = Credentials.basic(clientId, "")

fun createResponse(request: Request = createRequest()): Response {
    return Response.Builder()
        .request(request)
        .protocol(Protocol.HTTP_2)
        .code(401)
        .message("")
        .build()
}

fun createRequest(): Request = Request.Builder()
    .url("https://www.example.com")
    .build()

val randomInteger: Int
    get() = Random.nextInt()

val randomString: String
    get() = randomInteger.toString()
