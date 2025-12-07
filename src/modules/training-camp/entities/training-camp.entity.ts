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

@Entity('training_camps')
export class TrainingCamp {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column({ unique: true, name: 'zsxq_camp_id' })
  zsxqCampId: string;

  @Column({ length: 200 })
  name: string;

  @Column({ type: 'text', nullable: true })
  description?: string;

  @ManyToOne(() => Planet)
  @JoinColumn({ name: 'planet_id' })
  planet: Planet;

  @Column({ name: 'planet_id' })
  planetId: string;

  @Column({ type: 'date', name: 'start_date' })
  startDate: Date;

  @Column({ type: 'date', name: 'end_date' })
  endDate: Date;

  @Column({ name: 'participants_count', default: 0 })
  participantsCount: number;

  @CreateDateColumn({ name: 'created_at' })
  createdAt: Date;

  @UpdateDateColumn({ name: 'updated_at' })
  updatedAt: Date;
}
