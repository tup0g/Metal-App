CREATE TYPE user_status AS ENUM ('active', 'inactive', 'deleted', 'banned');
CREATE TYPE contact_status AS ENUM ('pending', 'accepted', 'blocked', 'rejected');
CREATE TYPE message_status AS ENUM ('sent', 'delivered', 'read');
CREATE TYPE media_type AS ENUM ('image', 'video', 'audio', 'document', 'other');
CREATE TYPE notification_type AS ENUM ('message', 'friend_request', 'group_invite', 'system');

-- Таблиця користувачів
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    status user_status NOT NULL DEFAULT 'active',
    avatar_url VARCHAR(255),
    last_online TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Індекси для таблиці користувачів
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_status_last_online ON users(status, last_online);

-- Таблиця контактів (дружби/підписок)
CREATE TABLE contacts (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    contact_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status contact_status NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT unique_contact UNIQUE(user_id, contact_id),
    CONSTRAINT not_self_contact CHECK (user_id != contact_id)
);

-- Індекси для таблиці контактів
CREATE INDEX idx_contacts_user_id ON contacts(user_id);
CREATE INDEX idx_contacts_contact_id ON contacts(contact_id);
CREATE INDEX idx_contacts_status ON contacts(status);

-- Таблиця особистих повідомлень
CREATE TABLE messages (
    id SERIAL PRIMARY KEY,
    sender_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recipient_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    content TEXT,
    status message_status NOT NULL DEFAULT 'sent',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT not_self_message CHECK (sender_id != recipient_id)
);

-- Індекси для таблиці повідомлень
CREATE INDEX idx_messages_sender_id ON messages(sender_id);
CREATE INDEX idx_messages_recipient_id ON messages(recipient_id);
CREATE INDEX idx_messages_created_at ON messages(created_at);
CREATE INDEX idx_messages_status ON messages(status);

-- Таблиця групових чатів
CREATE TABLE group_chats (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    avatar_url VARCHAR(255),
    created_by INTEGER NOT NULL REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Таблиця участі в групових чатах
CREATE TABLE group_chat_members (
    id SERIAL PRIMARY KEY,
    group_id INTEGER NOT NULL REFERENCES group_chats(id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    is_admin BOOLEAN NOT NULL DEFAULT false,
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT unique_membership UNIQUE(group_id, user_id)
);

-- Індекси для таблиці участі в групових чатах
CREATE INDEX idx_group_chat_members_group_id ON group_chat_members(group_id);
CREATE INDEX idx_group_chat_members_user_id ON group_chat_members(user_id);
CREATE INDEX idx_group_chat_members_is_admin ON group_chat_members(is_admin);

-- Таблиця повідомлень у групових чатах
CREATE TABLE group_messages (
    id SERIAL PRIMARY KEY,
    group_id INTEGER NOT NULL REFERENCES group_chats(id) ON DELETE CASCADE,
    sender_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    content TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Індекси для таблиці групових повідомлень
CREATE INDEX idx_group_messages_group_id ON group_messages(group_id);
CREATE INDEX idx_group_messages_sender_id ON group_messages(sender_id);
CREATE INDEX idx_group_messages_created_at ON group_messages(created_at);

-- Таблиця медіа-файлів
CREATE TABLE media_files (
    id SERIAL PRIMARY KEY,
    owner_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    filename VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    file_size INTEGER NOT NULL,
    file_type media_type NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    message_id INTEGER REFERENCES messages(id) ON DELETE CASCADE,
    group_message_id INTEGER REFERENCES group_messages(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT message_reference_check CHECK (
        (message_id IS NOT NULL AND group_message_id IS NULL) OR
        (message_id IS NULL AND group_message_id IS NOT NULL)
    )
);

-- Індекси для таблиці медіа-файлів
CREATE INDEX idx_media_files_owner_id ON media_files(owner_id);
CREATE INDEX idx_media_files_message_id ON media_files(message_id);
CREATE INDEX idx_media_files_group_message_id ON media_files(group_message_id);
CREATE INDEX idx_media_files_file_type ON media_files(file_type);

-- Таблиця push-сповіщень
CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    type notification_type NOT NULL,
    content TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT false,
    related_user_id INTEGER REFERENCES users(id) ON DELETE SET NULL,
    message_id INTEGER REFERENCES messages(id) ON DELETE CASCADE,
    group_message_id INTEGER REFERENCES group_messages(id) ON DELETE CASCADE,
    group_id INTEGER REFERENCES group_chats(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Індекси для таблиці сповіщень
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_is_read ON notifications(is_read);
CREATE INDEX idx_notifications_created_at ON notifications(created_at);
CREATE INDEX idx_notifications_type ON notifications(type);

-- Таблиця для зберігання статусу перегляду повідомлень
CREATE TABLE message_read_status (
    id SERIAL PRIMARY KEY,
    message_id INTEGER REFERENCES messages(id) ON DELETE CASCADE,
    group_message_id INTEGER REFERENCES group_messages(id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    read_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT message_type_check CHECK (
        (message_id IS NOT NULL AND group_message_id IS NULL) OR
        (message_id IS NULL AND group_message_id IS NOT NULL)
    ),
    CONSTRAINT unique_message_read_status UNIQUE(
        COALESCE(message_id, 0),
        COALESCE(group_message_id, 0),
        user_id
    )
);

-- Індекси для таблиці статусу перегляду повідомлень
CREATE INDEX idx_message_read_status_message_id ON message_read_status(message_id);
CREATE INDEX idx_message_read_status_group_message_id ON message_read_status(group_message_id);
CREATE INDEX idx_message_read_status_user_id ON message_read_status(user_id);