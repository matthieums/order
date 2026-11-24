import {Component, computed, input} from '@angular/core';

@Component({
  imports: [],
  selector: 'app-counter',
  styleUrl: './counter.css',
  templateUrl: './counter.html',
})
export class Counter {
  text = input<string>('');

  remainingCharacters = computed(() =>
    Math.max(0, 255 - this.text().length)
  );
}
