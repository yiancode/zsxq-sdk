package model

// TopicType 话题类型
type TopicType string

const (
	TopicTypeTalk     TopicType = "talk"
	TopicTypeTask     TopicType = "task"
	TopicTypeQA       TopicType = "q&a"
	TopicTypeSolution TopicType = "solution"
)

// Topic 话题模型
type Topic struct {
	TopicID       int64            `json:"topic_id"`
	TopicUID      string           `json:"topic_uid"`
	Group         *Group           `json:"group,omitempty"`
	Type          TopicType        `json:"type"`
	CreateTime    string           `json:"create_time"`
	Talk          *TalkContent     `json:"talk,omitempty"`
	Task          *TaskContent     `json:"task,omitempty"`
	Question      *QuestionContent `json:"question,omitempty"`
	Solution      *SolutionContent `json:"solution,omitempty"`
	LikesCount    int              `json:"likes_count,omitempty"`
	CommentsCount int              `json:"comments_count,omitempty"`
	RewardsCount  int              `json:"rewards_count,omitempty"`
	ReadingCount  int              `json:"reading_count,omitempty"`
	Digested      bool             `json:"digested,omitempty"`
	Sticky        bool             `json:"sticky,omitempty"`
}

// TalkContent 分享内容
type TalkContent struct {
	Owner   *User             `json:"owner"`
	Text    string            `json:"text,omitempty"`
	Images  []Image           `json:"images,omitempty"`
	Files   []FileAttachment  `json:"files,omitempty"`
	Article *Article          `json:"article,omitempty"`
}

// TaskContent 作业内容
type TaskContent struct {
	Owner *User  `json:"owner"`
	Title string `json:"title,omitempty"`
	Text  string `json:"text,omitempty"`
}

// QuestionContent 提问内容
type QuestionContent struct {
	Owner *User  `json:"owner"`
	Text  string `json:"text,omitempty"`
}

// SolutionContent 回答内容
type SolutionContent struct {
	Owner *User  `json:"owner"`
	Text  string `json:"text,omitempty"`
}

// Image 图片模型
type Image struct {
	ImageID   int64       `json:"image_id"`
	Type      string      `json:"type,omitempty"`
	Original  *ImageSize  `json:"original,omitempty"`
	Thumbnail *ImageSize  `json:"thumbnail,omitempty"`
	Large     *ImageSize  `json:"large,omitempty"`
}

// ImageSize 图片尺寸
type ImageSize struct {
	URL    string `json:"url"`
	Width  int    `json:"width"`
	Height int    `json:"height"`
	Size   int    `json:"size,omitempty"`
}

// FileAttachment 文件附件模型
type FileAttachment struct {
	FileID   int64  `json:"file_id"`
	Name     string `json:"name"`
	Hash     string `json:"hash,omitempty"`
	Size     int64  `json:"size,omitempty"`
	Duration int    `json:"duration,omitempty"`
	URL      string `json:"url,omitempty"`
}

// Article 文章模型
type Article struct {
	ArticleID         int64  `json:"article_id"`
	Title             string `json:"title"`
	InlineContentHTML string `json:"inline_content_html,omitempty"`
}

// Comment 评论模型
type Comment struct {
	CommentID  int64  `json:"comment_id"`
	Owner      *User  `json:"owner"`
	Text       string `json:"text"`
	CreateTime string `json:"create_time"`
	LikesCount int    `json:"likes_count,omitempty"`
	Repliee    *User  `json:"repliee,omitempty"`
	Sticky     bool   `json:"sticky,omitempty"`
}

// Hashtag 标签模型
type Hashtag struct {
	HashtagID   int64  `json:"hashtag_id"`
	Name        string `json:"name"`
	TopicsCount int    `json:"topics_count,omitempty"`
}
