import {
  Entity,
  Column,
  PrimaryGeneratedColumn,
  CreateDateColumn,
  UpdateDateColumn,
  ManyToOne,
  JoinColumn,
  ManyToMany,
} from 'typeorm';
import { User } from '@/modules/user/entities/user.entity';

@Entity('planets')
export class Planet {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column({ unique: true, name: 'zsxq_planet_id' })
  zsxqPlanetId: string;

  @Column({ length: 200 })
  name: string;

  @Column({ type: 'text', nullable: true })
  description?: string;

  @Column({ nullable: true })
  avatar?: string;

  @Column({ name: 'member_count', default: 0 })
  memberCount: number;

  @ManyToOne(() => User)
  @JoinColumn({ name: 'owner_id' })
  owner: User;

  @Column({ name: 'owner_id' })
  ownerId: string;

  @ManyToMany(() => User, (user) => user.planets)
  members: User[];

  @CreateDateColumn({ name: 'created_at' })
  createdAt: Date;

  @UpdateDateColumn({ name: 'updated_at' })
  updatedAt: Date;
}
