import {
  Entity,
  Column,
  PrimaryGeneratedColumn,
  CreateDateColumn,
  UpdateDateColumn,
  ManyToMany,
  JoinTable,
} from 'typeorm';
import { UserRole } from '../enums/user-role.enum';
import { Planet } from '@/modules/planet/entities/planet.entity';

@Entity('users')
export class User {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column({ unique: true, name: 'zsxq_user_id' })
  zsxqUserId: string;

  @Column({ length: 100 })
  username: string;

  @Column({ nullable: true })
  avatar?: string;

  @Column({
    type: 'enum',
    enum: UserRole,
    default: UserRole.USER,
  })
  role: UserRole;

  @Column({ nullable: true, select: false })
  password?: string;

  @ManyToMany(() => Planet)
  @JoinTable({
    name: 'user_planets',
    joinColumn: { name: 'user_id' },
    inverseJoinColumn: { name: 'planet_id' },
  })
  planets: Planet[];

  @CreateDateColumn({ name: 'created_at' })
  createdAt: Date;

  @UpdateDateColumn({ name: 'updated_at' })
  updatedAt: Date;
}
