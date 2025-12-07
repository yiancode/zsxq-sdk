import {
  Entity,
  Column,
  PrimaryGeneratedColumn,
  CreateDateColumn,
  UpdateDateColumn,
  ManyToOne,
  JoinColumn,
} from 'typeorm';
import { Planet } from '@/modules/planet/entities/planet.entity';
import { User } from '@/modules/user/entities/user.entity';

@Entity('topics')
export class Topic {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column({ unique: true, name: 'zsxq_topic_id' })
  zsxqTopicId: string;

  @Column({ length: 500 })
  title: string;

  @Column({ type: 'text' })
  content: string;

  @ManyToOne(() => Planet)
  @JoinColumn({ name: 'planet_id' })
  planet: Planet;

  @Column({ name: 'planet_id' })
  planetId: string;

  @ManyToOne(() => User)
  @JoinColumn({ name: 'author_id' })
  author: User;

  @Column({ name: 'author_id' })
  authorId: string;

  @Column({ name: 'likes_count', default: 0 })
  likesCount: number;

  @Column({ name: 'comments_count', default: 0 })
  commentsCount: number;

  @CreateDateColumn({ name: 'created_at' })
  createdAt: Date;

  @UpdateDateColumn({ name: 'updated_at' })
  updatedAt: Date;
}
