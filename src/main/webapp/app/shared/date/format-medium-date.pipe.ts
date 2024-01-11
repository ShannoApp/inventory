import { formatDate } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';

import dayjs from 'dayjs/esm';

@Pipe({
  standalone: true,
  name: 'formatMediumDate',
})
export default class FormatMediumDatePipe implements PipeTransform {
  transform(day: dayjs.Dayjs | null | undefined): string {
    return day ? formatDate(day.toString(), 'd MMM YYYY', 'en-US') : '';
  }
}
