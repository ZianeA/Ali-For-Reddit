package util.domain

import com.visualeap.aliforreddit.data.repository.token.CurrentTokenEntity
import com.visualeap.aliforreddit.data.repository.token.CurrentTokenEntity.*
import com.visualeap.aliforreddit.data.repository.token.TokenEntity
import com.visualeap.aliforreddit.data.repository.token.UserTokenEntity
import com.visualeap.aliforreddit.data.repository.token.UserlessTokenEntity
import com.visualeap.aliforreddit.data.repository.token.TokenResponse
import com.visualeap.aliforreddit.data.repository.comment.CommentEntity
import com.visualeap.aliforreddit.data.repository.comment.CommentResponse
import com.visualeap.aliforreddit.data.repository.redditor.RedditorEntity
import com.visualeap.aliforreddit.data.repository.post.PostEntity
import com.visualeap.aliforreddit.data.repository.post.PostResponse
import com.visualeap.aliforreddit.data.repository.post.PostWithSubredditEntity
import com.visualeap.aliforreddit.data.repository.post.PostWithSubredditResponse
import com.visualeap.aliforreddit.data.repository.redditor.RedditorResponse
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditEntity
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditResponse
import com.visualeap.aliforreddit.data.repository.token.TokenWithUserTokenEntity
import com.visualeap.aliforreddit.data.repository.token.TokenWithUserlessTokenEntity
import com.visualeap.aliforreddit.domain.model.*
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

//region Token
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

fun createTokenResponse(
    accessToken: String = ACCESS_TOKEN,
    type: String = TOKEN_TYPE,
    refreshToken: String? = REFRESH_TOKEN
) = TokenResponse(accessToken, type, refreshToken)

fun createTokenEntity(
    id: Int = ID,
    accessToken: String = ACCESS_TOKEN,
    type: String = TOKEN_TYPE
) = TokenEntity(id, accessToken, type)

fun createUserTokenEntity(id: Int = ID, refreshToken: String = REFRESH_TOKEN) =
    UserTokenEntity(id, refreshToken)

fun createUserlessTokenEntity(id: Int = ID, deviceId: String = DEVICE_ID) =
    UserlessTokenEntity(id, deviceId)

fun createTokenWithUserTokenEntity(
    token: TokenEntity = createTokenEntity(),
    userToken: UserTokenEntity = createUserTokenEntity()
) = TokenWithUserTokenEntity(token, userToken)

fun createTokenWithUserlessTokenEntity(
    token: TokenEntity = createTokenEntity(),
    userlessToken: UserlessTokenEntity = createUserlessTokenEntity()
) =
    TokenWithUserlessTokenEntity(token, userlessToken)

fun createCurrentTokenEntity(
    id: Int = SINGLE_RECORD_ID,
    tokenId: Int = ID,
    tokenType: TokenType = TokenType.USER
) = CurrentTokenEntity(id, tokenId, tokenType)
//endregion

fun createAccount(
    id: Int = ID,
    redditor: Redditor = createRedditor(),
    token: UserToken = createUserToken()
) = Account(id, redditor, token)

//region Redditor
private const val REDDITOR_USERNAME = "FakeUsername"
private const val REDDITOR_ID = "FakeRedditorId"

fun createRedditor(username: String = REDDITOR_USERNAME, id: String = REDDITOR_ID) =
    Redditor(username, id)

fun createRedditorEntity(username: String = REDDITOR_USERNAME, id: String = REDDITOR_ID) =
    RedditorEntity(username, id)

fun createRedditorReponse(username: String = REDDITOR_USERNAME, id: String = REDDITOR_ID) =
    RedditorResponse(RedditorResponse.Redditor(username, id))
//endregion

//region Subreddit
private const val SUBREDDIT_NAME = "FakeSubreddit"
private const val SUBREDDIT_ID = "FakeSubredditId"
private const val SUBREDDIT_ICON_URL = "https://www.redditstatic.com/avatars/test.png"
private const val SUBREDDIT_PRIMARY_COLOR = "#ffffff"
private const val SUBREDDIT_KEY_COLOR = "#000000"

fun createSubreddit(
    name: String = SUBREDDIT_NAME,
    id: String = SUBREDDIT_ID,
    iconUrl: String? = SUBREDDIT_ICON_URL,
    primaryColor: String? = SUBREDDIT_PRIMARY_COLOR,
    keyColor: String? = SUBREDDIT_KEY_COLOR
) =
    Subreddit(name, id, iconUrl, primaryColor, keyColor)

fun createSubredditEntity(
    name: String = SUBREDDIT_NAME,
    id: String = SUBREDDIT_ID,
    iconUrl: String? = SUBREDDIT_ICON_URL,
    primaryColor: String? = SUBREDDIT_PRIMARY_COLOR,
    keyColor: String? = SUBREDDIT_KEY_COLOR
) =
    SubredditEntity(name, id, iconUrl, primaryColor, keyColor)

fun createSubredditResponse(
    name: String = SUBREDDIT_NAME,
    id: String = SUBREDDIT_ID,
    iconUrl: String? = SUBREDDIT_ICON_URL,
    primaryColor: String? = SUBREDDIT_PRIMARY_COLOR,
    keyColor: String? = SUBREDDIT_KEY_COLOR
) = SubredditResponse(
    SubredditResponse.Data(
        listOf(
            SubredditResponse.Data.SubredditHolder(
                SubredditResponse.Data.SubredditHolder.Subreddit(
                    name,
                    id,
                    iconUrl,
                    primaryColor,
                    keyColor
                )
            )
        )
    )
)
//endregion

//region Post
private const val POST_ID = "FakePostId"
private const val POST_TITLE = "This is a fake post title"
private const val POST_TEXT = "This is a fake post text."
private const val POST_SCORE = 201
private const val POST_COMMENT_COUNT = 202
private const val POST_CREATED: Long = 1569878021

fun createPost(
    id: String = POST_ID,
    authorName: String = REDDITOR_USERNAME,
    title: String = POST_TITLE,
    text: String = POST_TEXT,
    score: Int = POST_SCORE,
    commentCount: Int = POST_COMMENT_COUNT,
    subreddit: Subreddit = createSubreddit(),
    created: Long = POST_CREATED
) = Post(id, authorName, title, text, score, commentCount, subreddit, created)

fun createPostEntity(
    id: String = POST_ID,
    authorName: String = REDDITOR_USERNAME,
    title: String = POST_TITLE,
    text: String = POST_TEXT,
    score: Int = POST_SCORE,
    commentCount: Int = POST_COMMENT_COUNT,
    subredditName: String = SUBREDDIT_NAME,
    created: Long = POST_CREATED
) = PostEntity(id, authorName, title, text, score, commentCount, subredditName, created)

fun createPostWithSubreddit(
    postEntity: PostEntity = createPostEntity(),
    subredditEntity: SubredditEntity = createSubredditEntity()
) = PostWithSubredditEntity(postEntity, subredditEntity)

fun createPostResponse(
    afterKey: String = "FAKE_AFTER_KEY",
    id: String = POST_ID,
    authorName: String = REDDITOR_USERNAME,
    title: String = POST_TITLE,
    text: String = POST_TEXT,
    score: Int = POST_SCORE,
    commentCount: Int = POST_COMMENT_COUNT,
    subredditId: String = SUBREDDIT_ID,
    created: Long = POST_CREATED
) = PostResponse(
    PostResponse.Data(
        afterKey,
        listOf(
            PostResponse.Data.PostHolder(
                PostResponse.Data.PostHolder.Post(
                    id,
                    authorName,
                    title,
                    text,
                    score,
                    commentCount,
                    subredditId,
                    created
                )
            )
        )
    )
)

fun createPostWithSubredditResponse(
    postResponse: PostResponse = createPostResponse(),
    subredditResponse: SubredditResponse = createSubredditResponse()
) = PostWithSubredditResponse(postResponse, subredditResponse)
//endregion

//region comment
const val COMMENT_ID = "FakeCommentId"
const val NESTED_COMMENT_ID = "FakeNestedCommentId"
private const val COMMENT_TEXT = "This is a fake comment text."
private const val COMMENT_SCORE = 201
private const val COMMENT_CREATION_DATE: Long = 1569878021
private const val COMMENT_DEPTH = 0
const val NESTED_COMMENT_DEPTH = 1

fun createComment(
    id: String = COMMENT_ID,
    authorName: String = REDDITOR_USERNAME,
    text: String = COMMENT_TEXT,
    score: Int = COMMENT_SCORE,
    creationDate: Long = COMMENT_CREATION_DATE,
    depth: Int = COMMENT_DEPTH,
    postId: String = POST_ID,
    parentId: String? = null,
    replies: List<Comment>? = listOf(
        createComment(
            id = NESTED_COMMENT_ID,
            parentId = COMMENT_ID,
            depth = NESTED_COMMENT_DEPTH,
            replies = null
        )
    )
) = Comment(id, authorName, text, score, creationDate, depth, postId, parentId, replies)

fun createCommentResponse(
    id: String = COMMENT_ID,
    authorName: String = REDDITOR_USERNAME,
    text: String = COMMENT_TEXT,
    score: Int = COMMENT_SCORE,
    creationDate: Long = COMMENT_CREATION_DATE,
    depth: Int = COMMENT_DEPTH,
    postId: String = POST_ID,
    parentId: String = POST_ID,
    replies: List<CommentResponse.Comment>? = createCommentResponse(
        id = NESTED_COMMENT_ID,
        parentId = COMMENT_ID,
        depth = NESTED_COMMENT_DEPTH,
        replies = null
    ).comments
) = CommentResponse(
    listOf(
        CommentResponse.Comment(
            id, authorName, text, score, creationDate, depth, postId, parentId, replies
        )
    )
)

fun createCommentEntity(
    id: String = COMMENT_ID,
    authorName: String = REDDITOR_USERNAME,
    text: String = COMMENT_TEXT,
    score: Int = COMMENT_SCORE,
    creationDate: Long = COMMENT_CREATION_DATE,
    depth: Int = COMMENT_DEPTH,
    postId: String = POST_ID,
    parentId: String? = null
) = CommentEntity(id, authorName, text, score, creationDate, depth, postId, parentId)
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
